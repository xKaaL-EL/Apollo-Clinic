function updateActiveSidebar(moduleName) {
    document.querySelectorAll('.sidebar-link').forEach(link => {
        link.classList.toggle('active', link.dataset.module === moduleName);
    });
}

document.querySelectorAll('.portal-footer').forEach(footer => {
    footer.textContent = '</> with \u2764\uFE0F by Piku & The Heroic KaaL-EL';
});
