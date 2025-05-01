async function handleLogin(event) {
    event.preventDefault();

    console.log("Login running..."); // sanity check

    const res = await fetch("/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        })
    });

    if (res.ok) {
        const data = await res.json();
        localStorage.setItem("token", data.jwt);
        window.location.href = "/task.html"; // make sure this matches your file name
    } else {
        alert("Invalid credentials, bro.");
    }
}