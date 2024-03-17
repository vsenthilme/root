import { Injectable } from '@angular/core';



interface HierarchyDatum {
  name: string;
  value?: any;
  color?: any;
  url?: any;
  children?: Array<HierarchyDatum>;
}

@Injectable({
  providedIn: 'root'
})
export class OutboundService {

  constructor() { }

 data: HierarchyDatum = {
    name: "TO-081440",
    value: '',
    children: [
      {
        name: "0057517552",
        value: '',
        children: [
          {
            name: "Outbound",
            value: '',
            color: 'red',
            children: 
            [
              {
                name: "Delivery No",
                value: '10400144982',
                color: 'red',
                children: undefined
               },
             {
              name: "Delivery Qty",
              value: '12',
              color: 'red',
              children: undefined
             },
             {
              name: "Barcode",
              value: '202108201803',
              color: 'red',
              children: undefined
             },
             {
              name: "Delivery Date",
              value: '29-07-2023 16:00',
              color: 'red',
              children: undefined
             }
            ]
          },
            {
            name: "Quality",
            value: '',
            color: 'blue',
            children:
            [
              {
               name: "Inspection No",
               value: '10300144896',
                color: 'blue',
               children: undefined
              },
              {
                name: "HE NO",
                value: 'R-TY04',
                color: 'blue',
                children: undefined
               },
               {
                 name: "Accepted Qty",
                 value: '12',
                 color: 'blue',
                 children: undefined
                },
                
              {
                name: "Inspection Date",
                value: '29-07-2023 14:40',
                color: 'blue',
                children: undefined
               }
             ]
          },
          {
            name: "Picking",
            value: '',
                color: 'green',
            children:
            [
              {
                name: "Pickup No",
                value: '1050010734',
                color: 'green',
                children: undefined
               },
              {
                name: "Picked Bin",
                value: '10',
                color: 'green',
                children: undefined
               },
               {
                 name: "Picked Qty",
                 value: '2',
                color: 'green',
                 children: undefined
                },
                {
                  name: "Picked Date",
                  value: '29-07-2023 14:25',
                 color: 'green',
                  children: undefined
                 }
             ]
          },
          {
            name: "Preoutbound",
            value: '',
                color: 'yellow',
            children:
            [
              {
                name: "Preoutbound No",
                value: '10100012774',
                color: 'yellow',
                url:'/main/inbound/putaway-create',
                children: undefined
               },
              {
                name: "Ordered Qty",
                value: '1060023027',
                color: 'yellow',
                children: undefined
               },
               {
                name: "Ordered Date",
                value: '29-07-2023 11:55',
                color: 'yellow',
                children: undefined 
               }
             ]
          }
        ]
      },
    ]
  };

}
