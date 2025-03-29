function buildTable(rows, columns, random = false) {
    let table = '<table>';
    for (let i = 0; i < rows; i++) {
        table += '<tr>';
        for (let j = 0; j < columns; j++) {
            const val1 = random ? (Math.random() * 20 - 10).toFixed(0) : 0;
            const val2 = random ? (Math.random() * 20 - 10).toFixed(0) : 0;
            table += `
                <td>
                    <input type="number" step="any" value="${val1}" class="matrix-input">
                    <input type="number" step="any" value="${val2}" class="matrix-input">
                </td>`;
        }
        table += '</tr>';
    }
    table += '</table>';
    $("#matrixContainer").html(table);
}

$("#generate").click(function() {
    const rows = parseInt($("#rows").val());
    const columns = parseInt($("#columns").val());
    buildTable(rows, columns);
});

$("#randomize").click(function() {
    const rows = parseInt($("#rows").val());
    const columns = parseInt($("#columns").val());
    buildTable(rows, columns, true);
});

$("form").submit(function() {
    const rows = $("#matrixContainer table tr").length;
    const columns = $("#matrixContainer table tr:first td").length;
    const matrix = [];

    $("#matrixContainer table tr").each(function() {
        const row = [];
        $(this).find("td").each(function() {
            const inputs = $(this).find("input");
            const p1 = parseFloat($(inputs[0]).val()) || 0;
            const p2 = parseFloat($(inputs[1]).val()) || 0;
            row.push([p1, p2]);
        });
        matrix.push(row);
    });

    $("#rowsField").val(rows);
    $("#columnsField").val(columns);
    $("#matrixDataField").val(JSON.stringify(matrix));
});
