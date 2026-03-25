// ---------------- AUTH ----------------

// Register new user
async function register() {
    const name = document.getElementById('reg-name').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;

    if (!name || !email || !password) {
        document.getElementById('reg-msg').innerText = "All fields are required";
        return;
    }

    try {
        const res = await fetch('http://localhost:3000/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password })
        });

        const data = await res.json();
        document.getElementById('reg-msg').innerText = data.message || data.error;
        if (data.message) {
            document.getElementById('reg-name').value = '';
            document.getElementById('reg-email').value = '';
            document.getElementById('reg-password').value = '';
        }
    } catch (err) {
        console.error(err);
        document.getElementById('reg-msg').innerText = "Server error, try again";
    }
}

// Login existing user
async function login() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
        const res = await fetch('http://localhost:3000/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const data = await res.json();

        if (data.user_id) {
            localStorage.setItem('user_id', data.user_id);
            localStorage.setItem('user_name', data.name);
            window.location.href = 'home.html';
        } else {
            document.getElementById('login-msg').innerText = data.error || 'Login failed';
        }
    } catch (err) {
        console.error('Fetch error:', err);
        document.getElementById('login-msg').innerText = 'Server error, try again';
    }
}

// Logout user
function logout() {
    localStorage.removeItem('user_id');
    localStorage.removeItem('user_name');
    window.location.href = 'login.html';
}

// ---------------- COURSES ----------------

// Load all courses (courses.html)
async function loadCoursesPage() {
    try {
        const res = await fetch('http://localhost:3000/courses');
        const courses = await res.json();

        const container = document.getElementById('courses-list');
        container.innerHTML = '';

        const emojis = ['💻','🎨','📊','🔬','📱','🧠','🎯','⚡','🌐','🛠️','📈','🎬'];

        courses.forEach((course, i) => {
            const div = document.createElement('div');
            div.className = 'course-card';
            div.innerHTML = `
                <h3>${course.title}</h3>
                <p>${course.description}</p>
                <p>Fees: ₹${course.fees}</p>
                <a href="course-details.html?id=${course.id}" class="btn">View Details</a>
            `;
            container.appendChild(div);
        });
    } catch (err) {
        console.error(err);
        document.getElementById('courses-list').innerText = "Failed to load courses";
    }
}

// Load single course details (course-details.html)
async function loadCourseDetailsPage() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;

    try {
        const res = await fetch(`http://localhost:3000/courses/${id}`);
        const course = await res.json();

        document.getElementById('course-title').innerText = course.title;
        document.getElementById('course-description').innerText = course.description;
        if (document.getElementById('course-syllabus'))
            document.getElementById('course-syllabus').innerText = course.syllabus;
        document.getElementById('course-fees').innerText = course.fees;
    } catch (err) {
        console.error(err);
        document.getElementById('course-title').innerText = "Failed to load course details";
    }
}

// ---------------- REGISTER COURSE (LEGACY - kept for compatibility) ----------------

// This is only used as a fallback; course-details.html now handles enrollment directly
function registerCourse() {
    const user_id = localStorage.getItem('user_id');
    const params = new URLSearchParams(window.location.search);
    const course_id = params.get('id');

    if (!user_id) {
        alert('Please login first');
        return;
    }

    fetch('http://localhost:3000/register-course', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ user_id, course_id, isFree: true })
    })
    .then(res => res.json())
    .then(data => {
        if (document.getElementById('course-msg'))
            document.getElementById('course-msg').innerText = data.message || data.error;
    })
    .catch(err => {
        console.error(err);
        if (document.getElementById('course-msg'))
            document.getElementById('course-msg').innerText = "Failed to register";
    });
}

function goToPayment() {
    const params = new URLSearchParams(window.location.search);
    const course_id = params.get('id');
    if (!course_id) {
        alert('Course ID not found');
        return;
    }
    window.location.href = `payment.html?course_id=${course_id}`;
}