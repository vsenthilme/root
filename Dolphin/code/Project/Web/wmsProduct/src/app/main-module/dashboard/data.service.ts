import { Injectable } from '@angular/core';
import { SpeedDial } from 'primeng/speeddial';



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
export class DataService {

  constructor() { }

 data: HierarchyDatum = {
    name: "TO-074459",
    value: '',
    children: [
      {
        name: "9028618",
        value: '',
        children: [
          {
            name: "Preinbound",
            value: '',
            color: 'red',
            children: 
            [
              {
                name: "Preinbound No",
                value: '1020000522',
                color: 'red',
                children: undefined
               },
             {
              name: "Order Qty",
              value: '21',
              color: 'red',
              children: undefined
             },
             {
              name: "Conatainer No ",
              value: 'SHOW ROOM RETURN',
              color: 'red',
              children: undefined
             }
            ]
          },
            {
            name: "Case Receipt",
            value: '',
            color: 'blue',
            children:
            [
              {
               name: "Case Code",
               value: '1040010725',
                color: 'blue',
               children: undefined
              },
              {
                name: "Case Qty",
                value: '21',
                color: 'blue',
                children: undefined
               }
             ]
          },
          {
            name: "Goods Receipt",
            value: '',
                color: 'green',
            children:
            [
              {
                name: "GR No",
                value: '1050010734',
                color: 'green',
                children: undefined
               },
              {
                name: "Accepted Qty",
                value: '10',
                color: 'green',
                children: undefined
               },
               {
                 name: "Damaged Qty",
                 value: '2',
                color: 'green',
                 children: undefined
                }
             ]
          },
          {
            name: "Putaway",
            value: '',
                color: 'yellow',
            children:
            [
              {
                name: "Putaway No",
                value: '1070022794',
                color: 'yellow',
                url:'/main/inbound/putaway-create',
                children: undefined
               },
              {
                name: "Pallet ID",
                value: '1060023027',
                color: 'yellow',
                children: undefined
               },
               {
                name: "Confirmed Bin",
                value: 'GB1BO24B06',
                color: 'yellow',
                children: undefined 
               },
               {
                 name: "Putaway Qty",
                 value: '2',
                color: 'yellow',
                 children: undefined
                }
             ]
          }
        ]
      },
      // {
      //   name: "9030207",
      //   value: '',
      //   children: [
      //     {
      //       name: "Preinbound",
      //       value: '1020000522',
      //       children: 
      //       [
      //        {
      //         name: "Order Qty",
      //         value: '6',
      //         children: undefined
      //        },
      //        {
      //         name: "Conatainer No ",
      //         value: 'SHOW ROOM RETURN',
      //         children: undefined
      //        }
      //       ]
      //     },
      //       {
      //       name: "Case Receipt",
      //       value: '1030000261',
      //       children:
      //       [
      //         {
      //          name: "Case Code",
      //          value: '1040010725',
      //          children: undefined
      //         },
      //         {
      //           name: "Case Qty",
      //           value: '6',
      //           children: undefined
      //          }
      //        ]
      //     },
      //     {
      //       name: "Goods Receipt",
      //       value: '1050010734',
      //       children:
      //       [
      //         {
      //           name: "Accepted Qty",
      //           value: '10',
      //           children: undefined
      //          },
      //          {
      //            name: "Damaged Qty",
      //            value: '2',
      //            children: undefined
      //           }
      //        ]
      //     },
      //     {
      //       name: "Putaway",
      //       value: '1070022794',
      //       children:
      //       [
      //         {
      //           name: "Pallet ID",
      //           value: '1060023027',
      //           children: undefined
      //          },
      //          {
      //            name: "Putaway Qty",
      //            value: '2',
      //            children: undefined
      //           }
      //        ]
      //     }
      //   ]
      // },
      // {
      //   name: "9030208",
      //   value: '',
      //   children: [
      //     {
      //       name: "Preinbound",
      //       value: '',
      //       children:undefined
      //     },
      //       {
      //       name: "Case Receipt",
      //       value: '',
      //       children:undefined
      //     },
      //     {
      //       name: "Goods Receipt",
      //       value: '',
      //       children:undefined
      //     },
      //     {
      //       name: "Putaway",
      //       value: '',
      //       children:undefined
      //     }
      //   ]
      // },
    ]
  };

}
