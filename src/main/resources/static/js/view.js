document.getElementById('player-select').addEventListener('change', function () {
    document.getElementById('maximin-player').value = this.value;
    document.getElementById('minimax-player').value = this.value;
});