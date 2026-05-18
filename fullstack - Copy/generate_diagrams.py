import base64
import json
import urllib.request
import os

diagrams = {
    "architecture.png": """%%{init: {'theme': 'base', 'themeVariables': { 'background': '#ffffff', 'primaryColor': '#ffffff', 'primaryBorderColor': '#333333', 'lineColor': '#333333', 'textColor': '#000000', 'tertiaryColor': '#f4f4f4'}}}%%
graph TD
    subgraph Users ["Users (Client)"]
        S[Student/Job Seeker]
        E[Employer]
        A[Admin]
    end
    subgraph Frontend ["Frontend Layer"]
        UI[Thymeleaf Templates + HTML/CSS/JS]
    end
    subgraph Backend ["Backend Framework (Spring Boot)"]
        C[Controllers - REST APIs]
        Sec[Spring Security - Auth/RBAC]
        Serv[Service Layer - Business Logic]
        R[Repository Layer - Spring Data JPA]
    end
    subgraph Data ["Data Storage"]
        DB[(MySQL Database)]
    end
    subgraph ThirdParty ["Third-Party Services"]
        MapAPI[Location Mapping API]
        BotAPI[AI Chatbot Service]
        Mail[OTP / Email Service]
    end
    S --> UI
    E --> UI
    A --> UI
    UI <--> Sec
    Sec <--> C
    C <--> Serv
    Serv <--> R
    R <--> DB
    UI <--> BotAPI
    UI <--> MapAPI
    Serv --> Mail
    style Users fill:#ffffff,stroke:#333,stroke-width:2px,color:#000
    style Frontend fill:#ffffff,stroke:#333,stroke-width:2px,color:#000
    style Backend fill:#ffffff,stroke:#333,stroke-width:2px,color:#000
    style Data fill:#ffffff,stroke:#333,stroke-width:2px,color:#000
    style ThirdParty fill:#ffffff,stroke:#333,stroke-width:2px,color:#000""",

    "mvc_lifecycle.png": """%%{init: {'theme': 'base', 'themeVariables': { 'background': '#ffffff', 'primaryColor': '#ffffff', 'primaryBorderColor': '#333333', 'lineColor': '#333333', 'textColor': '#000000'}}}%%
graph LR
    C[Client Browser]
    Ctrl[Controller Layer]
    Svc[Service Layer]
    Repo[Repository Layer]
    DB[(Database)]
    View[Thymeleaf View]
    C -- "1. HTTP Request" --> Ctrl
    Ctrl -- "2. Method Call" --> Svc
    Svc -- "3. CRUD Operations" --> Repo
    Repo -- "4. SQL Queries" --> DB
    DB -. "5. Result Set" .-> Repo
    Repo -. "6. Entity Objects" .-> Svc
    Svc -. "7. Processed Data" .-> Ctrl
    Ctrl -- "8. Passes Model" --> View
    View -. "9. Rendered HTML" .-> C""",

    "data_flow.png": """%%{init: {'theme': 'base', 'themeVariables': { 'background': '#ffffff', 'primaryColor': '#ffffff', 'primaryBorderColor': '#333333', 'lineColor': '#333333', 'textColor': '#000000', 'actorBkg': '#ffffff', 'actorBorder': '#333333', 'noteBkg': '#f9f9f9', 'noteBorder': '#333333'}}}%%
sequenceDiagram
    participant Student
    participant AuthSystem as Spring Security
    participant Controller as Application Controller
    participant Service as Application Service
    participant Database as MySQL Database

    Note over Student, Database: Data Flow: Student Job Application Process
    Student->>AuthSystem: 1. Login with Credentials
    AuthSystem-->>Student: 2. Request OTP
    Student->>AuthSystem: 3. Submit OTP
    AuthSystem-->>Student: 4. Auth Success (Session Created)
    
    Student->>Controller: 5. Browse Jobs (GET /jobs)
    Controller->>Database: 6. Fetch Active Jobs
    Database-->>Controller: 7. Return Jobs List
    Controller-->>Student: 8. Render Job Feed UI
    
    Student->>Controller: 9. Click 'Apply' & Upload Resume (POST)
    Controller->>Service: 10. Process Application Data & File
    Service->>Database: 11. Save JobApplication Entity
    Database-->>Service: 12. Confirm Save
    Service-->>Controller: 13. Application Success
    Controller-->>Student: 14. Display "Application Submitted Successfully" """
}

output_dir = "report_diagrams"
os.makedirs(output_dir, exist_ok=True)

for filename, mermaid_code in diagrams.items():
    j = json.dumps({"code": mermaid_code, "mermaid": {"theme": "default"}})
    b64 = base64.urlsafe_b64encode(j.encode('utf-8')).decode('utf-8')
    url = f"https://mermaid.ink/img/{b64}?type=png&bgColor=ffffff"
    
    try:
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
        with urllib.request.urlopen(req) as response:
            with open(os.path.join(output_dir, filename), 'wb') as out_file:
                out_file.write(response.read())
        print(f"Successfully generated {filename}")
    except Exception as e:
        print(f"Failed to generate {filename}: {e}")
