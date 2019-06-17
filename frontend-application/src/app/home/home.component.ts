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

          for(let i=0; i<d.data.labels.length; i++){
            if(d.data.dataset.label === "Distribución de bienes general")
              d.data.labels[i] += ": "+ d.data.dataset.data[i] + " BI";
            else
              d.data.labels[i] += ": "+ d.data.dataset.data[i];
          }

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
         var ancho = (80*width)/100;
         var alto = (40*height)/100;
         var x = (10*width)/100;
         var y = 0;
         var movY = (50*height)/100;

         for(let i=0; i<this.dashboards.length; i++)
         {
           let canvas = document.getElementById("chart"+i);

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

           doc.addImage(canvasImg, 'PNG', x, coordY, ancho, alto);
         }

         doc.save('dashboard.pdf');
       }
     }

}
