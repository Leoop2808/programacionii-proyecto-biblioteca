google.charts.load('current', {'packages':['corechart']});

google.charts.setOnLoadCallback(graficoPrincipal);

console.log("prueba");

function graficoPrincipal() {
      $.ajax({
        url: "api/dashboard/indicadores",
        dataType: "json",
      }).done(function (jsonData) {
        console.log(jsonData);
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'descripcion');
        data.addColumn('number', 'cantidad_no_devueltos');
        data.addColumn('number', 'cantidad_prestamos');
        data.addColumn('number', 'cantidad_solicitantes');
        
        data.addRow([
          jsonData.descripcion,
          jsonData.cantidad_no_devueltos,
          jsonData.cantidad_prestamos,
          jsonData.cantidad_solicitantes
        ]);

        var options = {
          chart: {
            width: 600,
            height: 400,
            title: 'Reporte de Prestamos',
            legend: { position: 'top'}
          }
        };

        var formatter = new google.visualization.NumberFormat({fractionDigits: 2} );
        formatter.format(data, 1);

        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options );
      }).fail(function (jq, text, err) {
        console.log(text + ' - ' + err);
      });
}
