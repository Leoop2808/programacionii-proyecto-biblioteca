google.charts.load('current', {'packages':['corechart']});

google.charts.setOnLoadCallback(graficoPrincipal);

function graficoPrincipal() {
      $.ajax({
        url: "api/dashboard/indicadores",
        dataType: "json",
      }).done(function (jsonData) {
        console.log(jsonData);
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'descripcion');
        data.addColumn('number', 'cantidad_prestamos');
        // data.addColumn('number', 'cantidad_no_devueltos');
        // data.addColumn('number', 'cantidad_prestamos');
        // data.addColumn('number', 'cantidad_solicitantes');
        
        data.addRow([
          'Cantidad de libros no devueltos',
          jsonData.cantidad_no_devueltos
        ]);

        data.addRow([
          'Cantidad de libros devueltos',
          jsonData.cantidad_prestamos - jsonData.cantidad_no_devueltos
        ]);
        // var options = {
        //   chart: {
        //     width: 600,
        //     height: 400,
        //     title: 'Reporte de Prestamos',
        //     legend: { position: 'top'}
        //   }
        // };

        var formatter = new google.visualization.NumberFormat({fractionDigits: 1} );
        formatter.format(data, 1);

        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data);
        
        var chart = new google.visualization.BarChart(document.getElementById('chart_div_2'));
        chart.draw(data);

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div_3'));
        chart.draw(data);

        // var wrap = new google.visualization.ChartWrapper();
        // wrap.setChartType('LineChart');
        // // wrap.setDataSourceUrl('http://spreadsheets.google.com/tq?key=pCQbetd-CptGXxxQIG7VFIQ&pub=1');
        // wrap.setdataSource(data);
        // wrap.setContainerId('chart_div');
        // wrap.setQuery('SELECT A,D WHERE D > 100 ORDER BY D');
        // wrap.setOptions({'title':'Population Density (people/km^2)', 'legend':'none'});
        // wrap.draw();
      }).fail(function (jq, text, err) {
        console.log(text + ' - ' + err);
      });
}
