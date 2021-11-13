google.charts.load('current', {'packages':['corechart']});

google.charts.setOnLoadCallback(graficoPrincipal);

function graficoPrincipal() {
      $.ajax({
        url: "api/dashboard/indicadores",
        dataType: "json",
      }).done(function (jsonData) {
        console.log(jsonData);

        $("#cant_solicitantes").html("" + jsonData.cantidad_solicitantes + " Solicitantes");

        var data = new google.visualization.DataTable();
        data.addColumn('string', 'descripcion');
        data.addColumn('number', 'Material');
        
        data.addRow([
          'Libros no devueltos',
          jsonData.cantidad_no_devueltos
        ]);

        data.addRow([
          'Libros devueltos',
          jsonData.cantidad_prestamos - jsonData.cantidad_no_devueltos
        ]);

        var formatter = new google.visualization.NumberFormat({fractionDigits: 1} );
        formatter.format(data, 1);

        var chart = new google.visualization.PieChart(document.getElementById('chart_div_dashboard'));
        chart.draw(data);
        
        var chart = new google.visualization.BarChart(document.getElementById('chart_div_dashboard_2'));
        chart.draw(data);

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div_dashboard_3'));
        chart.draw(data);

      }).fail(function (jq, text, err) {
        console.log(text + ' - ' + err);
      });
}
