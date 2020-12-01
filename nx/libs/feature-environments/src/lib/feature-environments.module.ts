import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EnvironmentsComponent } from './containers/environments/environments.component';
import { TargetsListComponent } from './components/targets-list/targets-list.component';
import { UiCommonsModule } from '@chutney/ui-commons';
import { RouterModule } from '@angular/router';
import { UiMaterialModule } from '@chutney/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EnvironmentsEditComponent } from './containers/environments-edit/environments-edit.component';

@NgModule({
  imports: [CommonModule,
    UiCommonsModule,
    RouterModule.forChild([
      {path: '', component: EnvironmentsComponent},
      {path: 'edit/:name', component: EnvironmentsEditComponent},
      {path: 'edit', component: EnvironmentsEditComponent},
    ]),
    UiMaterialModule,
    ReactiveFormsModule,
    FormsModule
  ],
  declarations: [EnvironmentsComponent, TargetsListComponent, EnvironmentsEditComponent],
  exports: [EnvironmentsComponent, EnvironmentsEditComponent],
})
export class FeatureEnvironmentsModule {
}
