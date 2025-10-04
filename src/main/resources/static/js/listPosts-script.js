document.addEventListener("DOMContentLoaded", function () {
    // Логика для кнопки удаления
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            let postId = this.getAttribute("data-post-id");

            fetch(`api/post/delete/${postId}`, {
                method: "DELETE"
            }).then(response => {
                if (response.ok) {
                    document.getElementById(`post-${postId}`).remove();
                } else {
                    alert("Ошибка при удалении");
                }
            }).catch(error => console.error("Ошибка:", error));
        });
    });

    // Логика для кнопки копирования ссылки
    document.querySelectorAll(".copy-btn").forEach(button => {
        button.addEventListener("click", function () {
            let postId = this.getAttribute("data-post-id");
            let link = `${window.location.origin}/post/${postId}`;

            navigator.clipboard.writeText(link)
                .then(() => alert("Ссылка скопирована: " + link))
                .catch(() => alert("Не удалось скопировать ссылку"));
        });
    });

    // Логика для кнопки редактирования
    document.querySelectorAll(".edit-btn").forEach(button => {
        button.addEventListener("click", function () {
            let postId = this.getAttribute("data-post-id");
            window.location.href = `/post/edit/${postId}`;
        });
    });
});
