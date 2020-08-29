class AuthService {
    login = (user) => fetch("/api/auth/login/" + user + "")
        .then(response => {
            if (response.ok) {
                localStorage.setItem("user", JSON.stringify(response.data))
            }
            return response.data;
        });

    register = (user) => fetch("/api/auth/login",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=utf-8"
            },
            body: JSON.stringify(user)
        });

    logout = () => localStorage.removeItem("user");

    getCurrentUser = () => {
        return JSON.parse(localStorage.getItem('user'));
    }
}

export default new AuthService();