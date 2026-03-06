<!DOCTYPE html>
<html>
<head>
    <title>To Do List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style> body { background-color: #6a5acd; color: white; } .card { background: rgba(255,255,255,0.1); border: none; } </style>
</head>
<body class="p-5">
    <div class="container card p-4 shadow-lg" style="max-width: 600px;">
        <h2 class="text-center mb-4">To Do List</h2> 
        <div class="input-group mb-3">
            <input type="text" id="taskInput" class="form-control" placeholder="Add a new task..."> 
            <button class="btn btn-primary" onclick="addTask()">Add</button> 
        </div>
        <ul id="taskList" class="list-group"></ul> 
    </div>

    <script>
        async function fetchTasks() {
            const res = await fetch('api/tasks'); 
            const tasks = await res.json();
            document.getElementById('taskList').innerHTML = tasks.map(t => `
                <li class="list-group-item d-flex justify-content-between align-items-center bg-transparent text-white border-light">
                    <span>${t.title}</span>
                    <div>
                        <i class="bi bi-pencil me-3 cursor-pointer" onclick="editTask(${t.id}, '${t.title}')"></i> 
                        <i class="bi bi-trash cursor-pointer text-danger" onclick="deleteTask(${t.id})"></i> 
                    </div>
                </li>
            `).join('');
        }

        async function addTask() {
            const title = document.getElementById('taskInput').value;
            await fetch('api/tasks', { method: 'POST', body: new URLSearchParams({title}) }); 
            document.getElementById('taskInput').value = '';
            fetchTasks(); 
        }

        async function editTask(id, oldTitle) {
            const title = prompt("New title:", oldTitle);
            if (title) {
                await fetch(`api/tasks/${id}?title=${encodeURIComponent(title)}`, { method: 'PUT' }); 
                fetchTasks();
            }
        }

        async function deleteTask(id) {
            await fetch(`api/tasks/${id}`, { method: 'DELETE' }); 
            fetchTasks(); 
        }
        fetchTasks();
    </script>
</body>
</html>