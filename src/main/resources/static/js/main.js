document.addEventListener("DOMContentLoaded", () => {
    console.log("Page loaded and JS works!");
});

const themeSwitch = document.getElementById('theme-switch');

// Проверяем состояние локального хранилища для сохраненной темы
const savedTheme = localStorage.getItem('theme');
if (savedTheme === 'dark') {
    document.body.classList.add('dark-mode');
    themeSwitch.checked = true;
}

// Изменяем тему и сохраняем в localStorage
themeSwitch.addEventListener('change', () => {
    const isDarkMode = themeSwitch.checked;
    document.body.classList.toggle('dark-mode', isDarkMode);
    localStorage.setItem('theme', isDarkMode ? 'dark' : 'light');
});
