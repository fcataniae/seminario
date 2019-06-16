import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { Dashboard } from '../model/bienes/dashboard.model';
import jsPDF from 'jspdf';
import { MovimientoService } from '../services/movimiento.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  chart1:Chart;
  chart2:Chart;
  chart3:Chart;
  chart4:Chart;
  charts:Chart[];
  dashboards: Dashboard[];

  constructor(private _movimientoService: MovimientoService) { }

  ngOnInit() {
    this.charts = [];
    this.charts.push(this.chart1);
    this.charts.push(this.chart2);
    this.charts.push(this.chart3);
    this.charts.push(this.chart4);

    this.dashboards = [];
    this._movimientoService.getDashboard()
    .subscribe(res=>{
        console.log(res);
        this.dashboards = res;
        this.generarGraficos();
      },
      error => console.log(error)
    );

   }//END OnInit

  generarGraficos(){

    console.log(this.dashboards);
    console.log(this.dashboards.length);
    console.log(this.charts);
     if(this.dashboards){
        let index = 0;
        let container = document.getElementById("container");
        console.log(container);
        this.dashboards.forEach(d => {
          let div = document.createElement("div");
          let canvas = document.createElement("canvas");

          div.classList.add("grafico");div.classList.add("mat-h2");div.classList.add("mat-elevation-z2");
          canvas.id = "chart"+index;
          div.setAttribute("style", "display: inline-block; width: 40vw; heigth: 40vh; margin-left:5vw;");
          div.appendChild(canvas);

          this.charts[index] = new Chart(canvas,{
            type: 'horizontalBar',
            data:{
              labels: d.data.labels,
              datasets:[{
                label: d.data.dataset.label,
                data:d.data.dataset.data,
                backgroundColor: d.data.dataset.backgroundColor
              }]
            },
            options: {
              title:{
                 text:d.data.dataset.label,
                 display:true,
                 fontSize: 18
              },
            }
          });
          container.appendChild(div);
          index++;
        });

      }
    }

    descargarPDF() {
      if(this.dashboards){
        //creo PDF con la imagenes
        var doc = new jsPDF('portrait','mm','a4');

        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();

        //tamaño y axis
        var ancho = width/2;
        var alto = height/3;
        var x = width/4;
        var y = 0;
        var movY = height/(1.7);

        for(let i=0; i<this.dashboards.length; i++)
        {
          var canvas = document.getElementById("chart"+i);
          console.log(canvas);

          //creo imagen
          var canvasCast = <HTMLCanvasElement> canvas;
          var canvasImg = canvasCast.toDataURL("image/png", 1.0);

          if(i % 2 === 0){
            if(i !== 0)//Agrego una pagina
              doc.addPage();
            var coordY = y;
          }else{
            var coordY = y + movY;
          }

          doc.addImage(canvasImg, 'PNG', x, coordY, ancho, alto );

          //Agregar lista
          let ul = document.createElement("ul");

          for(let j=0; j<this.dashboards[i].data.labels.length; j++){
            let liLabel = document.createElement("li");
            liLabel.textContent += this.dashboards[i].data.labels[j];
            liLabel.textContent += ": " + this.dashboards[i].data.dataset.data[j];
            ul.appendChild(liLabel);
          }

          doc.fromHTML(
            ul,
            5,
            coordY+alto,
            {'width': width}
          );

        }

        doc.save('dashboard.pdf');
      }
    }

}
