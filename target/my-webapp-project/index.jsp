<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>To Do List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background-color: #6a5acd; } /* Ödevdeki mor/mavi tema [cite: 196] */
        .todo-container { max-width: 500px; margin-top: 100px; }
        .card { border-radius: 15px; background: rgba(255, 255, 255, 0.2); backdrop-filter: blur(10px); color: white; border: none; }
        .list-group-item { background: rgba(255, 255, 255, 0.1); border: none; color: white; margin-bottom: 5px; border-radius: 10px !important; }
        .completed { text-decoration: line-through; opacity: 0.6; }
    </style>
</head>
<body>

<div class="container todo-container">
    <div class="card shadow p-4">
        <h2 class="text-center mb-4">To Do List</h2>
        
        <div class="input-group mb-4">
            <input type="text" id="taskInput" class="form-control" placeholder="Add a new task...">
            <button class="btn btn-primary" onclick="addTask()">Add</button>
        </div>

        <ul id="taskList" class="list-group">
            </ul>
    </div>
</div>

<script>
    // 1. Görevleri Listele (Read - GET) [cite: 146, 212]
    async function fetchTasks() {
        const response = await fetch('api/tasks');
        const tasks = await response.json();
        const list = document.getElementById('taskList');
        list.innerHTML = tasks.map(t => `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <div>
                    <input type="checkbox" class="form-check-input me-2" ${t.completed ? 'checked' : ''} disabled>
                    <span class="${t.completed ? 'completed' : ''}">${t.title}</span>
                </div>
                <div>
                    <i class="bi bi-pencil me-2 cursor-pointer"></i> <i class="bi bi-trash cursor-pointer text-danger" onclick="deleteTask(${t.id})"></i> </div>
            </li>
        `).join('');
    }

    // 2. Yeni Görev Ekle (Create - POST) [cite: 147, 212]
    async function addTask() {
        const input = document.getElementById('taskInput');
        if (!input.value.trim()) return;

        await fetch('api/tasks', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'title=' + encodeURIComponent(input.value)
        });
        
        input.value = '';
        fetchTasks(); // Sayfa yenilenmeden listeyi güncelle [cite: 127]
    }

    // 3. Görev Sil (Delete - DELETE) [cite: 149, 212]
    async function deleteTask(id) {
        if(confirm('Görevi silmek istediğine emin misin?')) {
            await fetch('api/tasks/' + id, { method: 'DELETE' });
            fetchTasks(); // Listeyi güncelle [cite: 127]
        }
    }

    // Sayfa açıldığında görevleri yükle
    fetchTasks();
</script>

</body>
</html>