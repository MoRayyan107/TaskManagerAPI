async function fetchTasks() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("No token found. Redirecting to login.");
        window.location.href = "/login.html";
        return;
    }

    const searchValue = document.getElementById("searchInput")?.value?.toLowerCase() || "";
    const sortBy = document.getElementById("sortSelect")?.value || "";
    let url = "/tasks";

    if (sortBy === "priority" || sortBy === "completed") {
        url = `/tasks/sorted?sort=${sortBy}`;
    }

    const res = await fetch(url, {
        method: "GET",
        headers: { "Authorization": "Bearer " + token }
    });

    if (res.ok) {
        let tasks = await res.json();

        tasks = tasks.filter(task =>
            task.title.toLowerCase().includes(searchValue) ||
            task.description.toLowerCase().includes(searchValue) ||
            task.priority.toLowerCase().includes(searchValue)
        );

        const activeList = document.getElementById("activeTasks");
        const completedList = document.getElementById("completedTasks");

        tasks.forEach(task => {
            if (activeList.children.length === 0 && completedList.children.length === 0) {
                const emptyMessage = document.createElement("div");
                emptyMessage.textContent = "No tasks found.";
                emptyMessage.classList.add("empty-box");

                activeList.appendChild(emptyMessage); // or choose where to show it
            }
            const li = document.createElement("li");

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.checked = task.completed;

            checkbox.onchange = () => {
                updateTask(task.id, {
                    title: task.title,
                    description: task.description,
                    priority: task.priority,
                    completed: checkbox.checked
                });
            };


            const left = document.createElement("div");
            left.classList.add("task-title");
            if (task.completed) {
                left.classList.add("task-completed");
            }

            const titleSpan = document.createElement("span");
            titleSpan.textContent = `${task.title} - ${task.description}`;
            left.appendChild(titleSpan);

            const priorityBadge = document.createElement("span");
            priorityBadge.classList.add("priority-badge");
            if (task.priority === "LOW") {
                priorityBadge.classList.add("priority-low");
                priorityBadge.textContent = "LOW";
            } else if (task.priority === "MEDIUM") {
                priorityBadge.classList.add("priority-medium");
                priorityBadge.textContent = "MEDIUM";
            } else if (task.priority === "HIGH") {
                priorityBadge.classList.add("priority-high");
                priorityBadge.textContent = "HIGH";
            }
            left.appendChild(priorityBadge);

            const actions = document.createElement("div");
            actions.classList.add("task-actions");

            const deleteBtn = document.createElement("button");
            deleteBtn.textContent = "Delete";
            deleteBtn.onclick = () => deleteTask(task.id);

            actions.appendChild(deleteBtn);

            li.appendChild(checkbox);
            li.appendChild(left);
            li.appendChild(actions);

            if (task.completed) {
                completedList.appendChild(li);
            } else {
                activeList.appendChild(li);
            }
        });

    } else if (res.status === 403 || res.status === 401) {
        alert("Session expired or access denied. Redirecting to login.");
        localStorage.removeItem("token");
        window.location.href = "/login.html";
    } else {
        alert("Error fetching tasks.");
    }
}

function updateTask(id, data) {
    const token = localStorage.getItem("token");

    fetch(`/tasks/update/${id}`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(res => {
        if (res.ok) {
            fetchTasks();
        } else {
            alert("Failed to update task.");
        }
    });
}


function deleteTask(id) {
    const token = localStorage.getItem("token");
    fetch(`/tasks/delete/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    }).then(fetchTasks);
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}

fetchTasks();