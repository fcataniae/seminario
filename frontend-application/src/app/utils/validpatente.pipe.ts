import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'validpatente'
})
export class ValidpatentePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if(value){
      let array = Array.from(value.toLocaleLowerCase());
      array = array.filter( a => a !== " ");
      let charArray = array.filter(c => c.toString().match(/[a-z]/) );
      let numberArray = array.filter( n => n.toString().match(/[0-9]/));
      console.log(charArray);
      console.log(numberArray);
      let returnValue= "";
      value.substring(0,10);
      if(charArray.length < 4 ){
        charArray.forEach( v => returnValue += v);
        returnValue += " ";
        let numb = numberArray.splice(0,3);
        numb.forEach( v => returnValue += v);
        value = returnValue.substring(0,7).toLocaleUpperCase();
      }else if(charArray.length = 4){
        let first = charArray.splice(0,2);
        let second = charArray.splice(0,2);
        let midle = numberArray.splice(0,3);
        first.forEach(v => returnValue+=v);
        returnValue += " ";
        midle.forEach(v => returnValue+=v);
        returnValue += " ";
        second.forEach(v => returnValue+=v);
        value = returnValue.substring(0,10).toLocaleUpperCase();
      }else{
        console.log(value);
      }
    }
    return value;
  }

}
