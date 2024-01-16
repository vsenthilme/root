import {
    Injectable
}
    from '@angular/core';
import * as XLSX from 'xlsx';
@Injectable({
    providedIn: 'root'
})
export class ExcelService {
    exportAsExcel(data: any, filename: string = "Downloaded_Excel") {
        // const asnConf = data;

        const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(data);
        // converts a DOM TABLE element to a worksheet
        const wb: XLSX.WorkBook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
        /* save to file */
        XLSX.writeFile(
            wb,
            filename + `_${new Date().getDate() +
            '-' +
            (new Date().getMonth() + 1) +
            '-' +
            new Date().getFullYear()
            }.xlsx`
        );
    }
}