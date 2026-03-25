// ------------------- DEPENDENCIES -------------------
const express = require('express');
const mysql = require('mysql2');
const bcrypt = require('bcrypt');
const bodyParser = require('body-parser');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');


const app = express();
const port = 3000;

const uploadDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir);
}


const upload = multer({ dest: 'uploads/' });

app.use(cors());
app.use(bodyParser.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(__dirname));

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'academy'
});

db.connect(err => {
    if (err) throw err;
    console.log('✅ Connected to MySQL database');
});


app.post('/register', async (req, res) => {
    const { name, email, password } = req.body;

    if (!name || !email || !password)
        return res.status(400).json({ error: 'All fields are required' });

    try {
        const hashedPassword = await bcrypt.hash(password, 10);

        const sql = 'INSERT INTO users (name, email, password) VALUES (?, ?, ?)';
        db.query(sql, [name, email, hashedPassword], (err, result) => {
            if (err) {
                if (err.code === 'ER_DUP_ENTRY')
                    return res.status(400).json({ error: 'Email already registered' });
                return res.status(500).json({ error: err.message });
            }
            res.json({ message: 'User registered successfully', user_id: result.insertId });
        });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// ---------- LOGIN ----------
app.post('/login', (req, res) => {
    const { email, password } = req.body;

    if (!email || !password)
        return res.status(400).json({ error: 'All fields are required' });

    const sql = 'SELECT * FROM users WHERE email = ?';

    db.query(sql, [email], async (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        if (results.length === 0)
            return res.status(400).json({ error: 'User not found' });

        const user = results[0];
        const match = await bcrypt.compare(password, user.password);

        if (!match)
            return res.status(400).json({ error: 'Incorrect password' });

        res.json({ message: `Welcome ${user.name}`, user_id: user.id, name: user.name });
    });
});

// ---------- GET ALL COURSES ----------
app.get('/courses', (req, res) => {
    const sql = 'SELECT id, title, description, fees FROM courses';
    db.query(sql, (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// ---------- GET COURSE BY ID ----------
app.get('/courses/:id', (req, res) => {
    const sql = 'SELECT * FROM courses WHERE id = ?';
    db.query(sql, [req.params.id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        if (results.length === 0)
            return res.status(404).json({ error: 'Course not found' });
        res.json(results[0]);
    });
});

// ---------- REGISTER COURSE (FREE + PAID) ----------
// This route handles BOTH:
//   - Free courses: sent as JSON with { user_id, course_id, isFree: true }
//   - Paid courses: sent as multipart/form-data with receipt file
app.post('/register-course', (req, res) => {
    // Check content type to decide parsing strategy
    const contentType = req.headers['content-type'] || '';

    if (contentType.includes('multipart/form-data')) {
        // PAID COURSE — use multer to parse file
        upload.single('receipt')(req, res, (err) => {
            if (err) return res.status(500).json({ error: err.message });

            const { user_id, course_id } = req.body;
            const receiptFile = req.file;

            if (!user_id || !course_id)
                return res.status(400).json({ error: 'User ID and Course ID are required' });

            if (!receiptFile)
                return res.status(400).json({ error: 'Payment receipt is required' });

            // Check duplicate
            const checkSql = 'SELECT * FROM registrations WHERE user_id = ? AND course_id = ?';
            db.query(checkSql, [user_id, course_id], (err, results) => {
                if (err) return res.status(500).json({ error: err.message });
                if (results.length > 0)
                    return res.status(400).json({ error: 'Already registered for this course' });

                const insertSql = 'INSERT INTO registrations (user_id, course_id, receipt_path) VALUES (?, ?, ?)';
                db.query(insertSql, [user_id, course_id, receiptFile.path], (err) => {
                    if (err) return res.status(500).json({ error: err.message });
                    res.json({ message: 'Payment submitted successfully. You will be enrolled within 24 hours.' });
                });
            });
        });

    } else {
        // FREE COURSE — already parsed as JSON by bodyParser
        const { user_id, course_id, isFree } = req.body;
        const isFreeCourse = isFree === true || isFree === 'true';

        if (!user_id || !course_id)
            return res.status(400).json({ error: 'User ID and Course ID are required' });

        if (!isFreeCourse)
            return res.status(400).json({ error: 'Payment receipt is required for paid courses' });

        // Check duplicate
        const checkSql = 'SELECT * FROM registrations WHERE user_id = ? AND course_id = ?';
        db.query(checkSql, [user_id, course_id], (err, results) => {
            if (err) return res.status(500).json({ error: err.message });
            if (results.length > 0)
                return res.status(400).json({ error: 'Already registered for this course' });

            const insertSql = 'INSERT INTO registrations (user_id, course_id, receipt_path) VALUES (?, ?, ?)';
            db.query(insertSql, [user_id, course_id, null], (err) => {
                if (err) return res.status(500).json({ error: err.message });
                res.json({ message: 'Free course enrolled successfully!' });
            });
        });
    }
});

// ---------- GET ENROLLED COURSES ----------
app.get('/enrolled-courses/:user_id', (req, res) => {
    const user_id = req.params.user_id;

    // Use CAST to avoid int/varchar type mismatch in JOIN
    const sql = `
        SELECT c.id, c.title, c.description, c.fees
        FROM registrations r
        JOIN courses c ON CAST(r.course_id AS CHAR) = CAST(c.id AS CHAR)
        WHERE CAST(r.user_id AS CHAR) = CAST(? AS CHAR)
    `;

    db.query(sql, [user_id], (err, results) => {
        if (err) {
            console.error('enrolled-courses error:', err.message);
            return res.status(500).json({ error: err.message });
        }
        console.log(`enrolled-courses: user_id=${user_id} => ${results.length} result(s)`);
        res.json(results);
    });
});

// ---------- GET LESSONS ----------
app.get('/lessons/:course_id', (req, res) => {
    const sql = 'SELECT * FROM lessons WHERE course_id = ? ORDER BY id ASC';
    db.query(sql, [req.params.course_id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// ------------------- START SERVER -------------------
app.listen(port, () => {
    console.log(`🚀 Server running at http://localhost:${port}`);
});