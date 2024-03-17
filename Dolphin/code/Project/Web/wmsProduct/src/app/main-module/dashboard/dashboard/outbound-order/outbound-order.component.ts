
import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import * as d3 from "d3";
import { NgxSpinnerService } from 'ngx-spinner';
import { Router } from '@angular/router';
import { PutawayService } from 'src/app/main-module/Inbound/putaway/putaway.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { OutboundService } from '../../outbound.service';



@Component({
  selector: 'app-outbound-order',
  templateUrl: './outbound-order.component.html',
  styleUrls: ['./outbound-order.component.scss']
})
export class OutboundOrderComponent implements OnInit {

 title = 'd3tree';
  @ViewChild('chart', { static: true }) private chartContainer: ElementRef;


  root: any;
  tree: any;
  treeLayout: any;
  svg: any;

  treeData: any;

  height: number;
  width: number;
  margin: any = { top: 250, bottom: 90, left: 130, right: 90 };
  duration: number = 750;
  nodeWidth: number = 5;
  nodeHeight: number = 1;
  nodeRadius: number = 5;
  horizontalSeparationBetweenNodes: number = 0;
  verticalSeparationBetweenNodes: number = 5;
  nodeTextDistanceY: string = "-5px";
  nodeTextDistanceX: number = 5;

  dragStarted: boolean;
  draggingNode: any;
  nodes: any[];
  selectedNodeByDrag: any;

  selectedNodeByClick: any;
  previousClickedDomNode: any;
  links: any;
  itemCode: any[] = [];

  toList: any[] = [];
  
  constructor(private data: OutboundService, private spin: NgxSpinnerService,  private router: Router,
    private putaway: PutawayService, private cs: CommonService) {

    this.toList = [
      {value: 'TO-081440', label: 'TO-081440'},
      {value: 'TO-075640', label: 'TO-075640'},
      {value: 'TO-078863', label: 'TO-078863'},
      {value: 'TO-079127', label: 'TO-079127'},
  ];
  this.itemCode = [
    {value: '0057517552', label: '0057517552'},
    {value: '0057517553', label: '0057517553'},
    {value: '0057517554', label: '0057517554'},
    {value: '0057517555', label: '0057517555'},
];
  
   }

  ngOnInit() {

  }

  showDashboard(){
    this.spin.show()
    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.renderTreeChart();
      this.spin.hide();
    }, 1000);

 
  }

  renderTreeChart() {
    this.spin.hide();
    let element: any = this.chartContainer.nativeElement;
    this.width = element.offsetWidth - this.margin.left - this.margin.right;
    this.height = element.offsetHeight - this.margin.top - this.margin.bottom;

    this.svg = d3.select(element).append('svg')
      .attr('width', element.offsetWidth)
      .attr('height', element.offsetHeight)
      .append("g")
      .attr('transform', 'translate(' + this.margin.left + ',' + this.margin.top + ')');

    // declares a tree layout and assigns the size
    this.tree = d3.tree()
      .size([this.height, this.width])
      .nodeSize([this.nodeWidth + this.horizontalSeparationBetweenNodes, this.nodeHeight + this.verticalSeparationBetweenNodes])
      .separation((a, b) => { return a.parent == b.parent ? 5 : 10 });

    // Assigns parent, children, height, depth
    this.root = d3.hierarchy(this.data.data, (d) => { return d.children; });
    this.root.x0 = this.height / 4;
    this.root.y0 = 10;

    // Collapse after the second level
   // this.root.children.forEach(collapse);

    this.updateChart(this.root);

    // function collapse(d) {
    //   if (d.children) {
    //       d._children = d.children;
    //       d._children.forEach(collapse);
    //       d.children = null;
    //   }
    // }

  }

  click = (d) => {
    if(d.children == null && d._children == null){
      this.navigate(d)
    }
    if (d.children) {
      d._children = d.children;
      d.children = null;
      console.log('start')
    } else {
      d.children = d._children;
      d._children = null;
      console.log('end')
    }
    this.updateChart(d);
  }

  updateChart(source) {
    let i = 0;
    console.log(source);
    this.treeData = this.tree(this.root);
    this.nodes = this.treeData.descendants();
    this.links = this.treeData.descendants().slice(1);
    this.nodes.forEach((d) => { d.y = d.depth * 180 });

    let node = this.svg.selectAll('g.node')
      .data(this.nodes, (d) => { return d.id || (d.id = ++i); });

    let nodeEnter = node.enter().append('g')
      .attr('class', 'node')
      .attr('transform', (d) => {
        return 'translate(' + source.y0 + ',' + source.x0 + ')';
      })
      
      .on('click', this.click);

    nodeEnter.append('circle')
      .attr('class', 'node')
      .attr('r', 1e-2)
      .style('fill', (d) => {
        return d._children ? 'lightsteelblue' : '#fff';
      });

    nodeEnter.append('text')
      .attr('dy', '.35em')
      .attr('x', (d) => {
        return d.children || d._children ? -13 : 13;
      })
      .attr('text-anchor', (d) => {
        return d.children || d._children ? 'end' : 'start';
      })
      .style('font', '12px sans-serif')
     // .style('color', 'steelblue')
     .style('color', (d) => {
      return d.data.color ? d.data.color : '#adadad';    
    })
      .text((d) => { return d.data.name +  (d.data.value ? ' - ' + d.data.value : '')});
    let nodeUpdate = nodeEnter.merge(node);

    nodeUpdate.transition()
      .duration(this.duration)
      .attr('transform', (d) => {
        return 'translate(' + d.y + ',' + d.x + ')';
      });

    nodeUpdate.select('circle.node')
      .attr('r', 3)
      .style('stroke-width', '1px')
      //.style('stroke', 'steelblue')
      .style('fill', (d) => {
        return d._children ? '#5a5a5a' : '#5a5a5a';
      })
      .attr('cursor', 'pointer');

    let nodeExit = node.exit().transition()
      .duration(this.duration)
      .attr('transform', (d) => {
        return 'translate(' + source.y + ',' + source.x + ')';
      })
      .remove();

    nodeExit.select('circle')
      .attr('r', 1e-2);

    nodeExit.select('text')
      .style('fill-opacity', 1e-2);

    let link = this.svg.selectAll('path.link')
      .data(this.links, (d) => { return d.id; });

    let linkEnter = link.enter().insert('path', 'g')
      .attr('class', 'link')
      .style('fill', 'none')
      //.style('stroke', '#adadad')
      .style('stroke', (d) => {
        return d.data.color ? d.data.color : '#adadad';    
      })
      .style('stroke-width', '1px')
      .attr('d', function (d) {
        let o = { x: source.x0, y: source.y0 };
        return diagonal(o, o);
      });

    let linkUpdate = linkEnter.merge(link);

    linkUpdate.transition()
      .duration(this.duration)
      .attr('d', (d) => { return diagonal(d, d.parent); });

    let linkExit = link.exit().transition()
      .duration(this.duration)
      .attr('d', function (d) {
        let o = { x: source.x, y: source.y };
        return diagonal(o, o);
      })
      .remove();

    this.nodes.forEach((d) => {
      d.x0 = d.x;
      d.y0 = d.y;
    });

    function diagonal(s, d) {
      let path = `M ${s.y} ${s.x}
                  C ${(s.y + d.y) / 2} ${s.x},
                  ${(s.y + d.y) / 2} ${d.x},
                  ${d.y} ${d.x}`;
      return path;
    }
  }



  navigate(d){
    let paramdata = "";
    console.log(d.data.value)
    if(d.data.name = "Putaway No"){
      this.putaway.search({putAwayNumber: [d.data.value]}).subscribe(res => {
        paramdata = this.cs.encrypt({ code: res, pageflow: 'Display' });
        this.router.navigate(['/main/inbound/putaway-create/' + paramdata]);
      })
    }
    //this.router.navigate([d.data.url]);
  }



}



        // return d.data.color == 'red' ? 'red' : d.data.color == 'blue' ? 'blue' : d.data.color == 'green' ? 'green'
        //  : d.data.color == 'yellow' ? 'yellow' : '#adadad';