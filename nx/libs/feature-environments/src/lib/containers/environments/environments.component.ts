import { Component, OnInit } from '@angular/core';
import { Environment, EnvironmentsGQL } from '@chutney/data-access';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';

@Component({
  selector: 'chutney-environments',
  templateUrl: './environments.component.html',
  styleUrls: ['./environments.component.scss']
})
export class EnvironmentsComponent implements OnInit {

  environments$: Observable<Environment[]>;
  envControl = new FormControl('', Validators.required);

  breadcrumbs: any = [
    { title: 'Home', link: ['/'] },
    { title: 'Environments', link: ['/'] },
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private environmentsGQL: EnvironmentsGQL) { }

  ngOnInit(): void {

    this.environments$ = this.environmentsGQL.watch().valueChanges.pipe(map((result) => {
      if(result.data.environments.length > 0) {
        this.envControl.setValue(result.data.environments[0])
      }
      return result.data.environments
    }));

  }

  createEnvironmentEvent() {
    this.router.navigate(['edit'], { relativeTo: this.route });
  }
}
