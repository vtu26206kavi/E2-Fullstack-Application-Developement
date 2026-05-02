document.addEventListener('DOMContentLoaded', function() {
    // Inject Chatbot HTML
    const chatbotHTML = `
        <div id="custom-chatbot" style="position: fixed; bottom: 20px; right: 20px; z-index: 9999; font-family: 'Inter', sans-serif;">
            
            <!-- Chat Window -->
            <div id="chatbot-window" style="display: none; width: 300px; height: 400px; background: #fff; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.2); overflow: hidden; display: flex; flex-direction: column; transition: all 0.3s ease; opacity: 0; transform: translateY(20px); pointer-events: none;">
                
                <!-- Header -->
                <div style="background: linear-gradient(135deg, #6366f1, #a855f7); color: white; padding: 15px; font-weight: 600; display: flex; justify-content: space-between; align-items: center;">
                    <span>JobPortal Assistant</span>
                    <button id="chatbot-close" style="background: none; border: none; color: white; cursor: pointer; font-size: 1.2rem;">&times;</button>
                </div>
                
                <!-- Messages -->
                <div id="chatbot-messages" style="flex: 1; padding: 15px; overflow-y: auto; background: #f8fafc; color: #333; font-size: 0.9rem;">
                    <div style="margin-bottom: 10px; text-align: left;">
                        <span style="background: #e2e8f0; padding: 8px 12px; border-radius: 12px; display: inline-block;">Hello! How can I help you find your dream job today?</span>
                    </div>
                </div>
                
                <!-- Input -->
                <div style="padding: 10px; background: white; border-top: 1px solid #e2e8f0; display: flex; gap: 8px;">
                    <input type="text" id="chatbot-input" placeholder="Type a message..." style="flex: 1; padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 20px; outline: none; font-size: 0.9rem;">
                    <button id="chatbot-send" style="background: #6366f1; color: white; border: none; border-radius: 50%; width: 35px; height: 35px; cursor: pointer; display: flex; align-items: center; justify-content: center;">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                    </button>
                </div>
            </div>

            <!-- Chat Button -->
            <button id="chatbot-toggle" style="position: absolute; bottom: 0; right: 0; width: 60px; height: 60px; border-radius: 50%; background: linear-gradient(135deg, #6366f1, #a855f7); color: white; border: none; box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4); cursor: pointer; display: flex; align-items: center; justify-content: center; transition: transform 0.2s ease;">
                <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>
            </button>
            
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', chatbotHTML);

    // Chatbot Logic
    const toggleBtn = document.getElementById('chatbot-toggle');
    const closeBtn = document.getElementById('chatbot-close');
    const chatWindow = document.getElementById('chatbot-window');
    const inputField = document.getElementById('chatbot-input');
    const sendBtn = document.getElementById('chatbot-send');
    const messagesContainer = document.getElementById('chatbot-messages');

    let isOpen = false;

    function toggleChat() {
        isOpen = !isOpen;
        if (isOpen) {
            chatWindow.style.opacity = '1';
            chatWindow.style.transform = 'translateY(-70px)';
            chatWindow.style.pointerEvents = 'auto';
            toggleBtn.style.transform = 'scale(0)';
        } else {
            chatWindow.style.opacity = '0';
            chatWindow.style.transform = 'translateY(20px)';
            chatWindow.style.pointerEvents = 'none';
            toggleBtn.style.transform = 'scale(1)';
        }
    }

    toggleBtn.addEventListener('click', toggleChat);
    closeBtn.addEventListener('click', toggleChat);

    function sendMessage() {
        const text = inputField.value.trim();
        if (!text) return;

        // User Message
        messagesContainer.innerHTML += `
            <div style="margin-bottom: 10px; text-align: right;">
                <span style="background: #6366f1; color: white; padding: 8px 12px; border-radius: 12px; display: inline-block;">${text}</span>
            </div>
        `;
        inputField.value = '';
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        // Bot Response
        setTimeout(() => {
            const replies = [
                "That sounds great! Make sure your resume is up to date.",
                "I'm a simple mock assistant, but I believe in you!",
                "Have you checked our latest postings on the Dashboard?",
                "If you have any technical issues, please contact support."
            ];
            const randomReply = replies[Math.floor(Math.random() * replies.length)];
            
            messagesContainer.innerHTML += `
                <div style="margin-bottom: 10px; text-align: left;">
                    <span style="background: #e2e8f0; padding: 8px 12px; border-radius: 12px; display: inline-block;">${randomReply}</span>
                </div>
            `;
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }, 1000);
    }

    sendBtn.addEventListener('click', sendMessage);
    inputField.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') sendMessage();
    });
});
