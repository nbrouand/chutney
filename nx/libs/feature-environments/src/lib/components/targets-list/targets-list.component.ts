import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { Environment, Target } from '@chutney/data-access';
import { isNullOrUndefined } from 'util';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'chutney-targets-list',
  templateUrl: './targets-list.component.html',
  styleUrls: ['./targets-list.component.scss']
})
export class TargetsListComponent implements OnInit {

  _targetsDataSource: MatTableDataSource<Target> = new MatTableDataSource<Target>();
  displayedColumns: string[] = ['name', 'url', 'action'];


  @Input() set targets(targets: Target[]) {
    if (!isNullOrUndefined(targets)) {
      this._targetsDataSource.data = targets;
    }
  }

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor() {
  }

  ngOnInit(): void {
    this._targetsDataSource.sort = this.sort;
  }

}
