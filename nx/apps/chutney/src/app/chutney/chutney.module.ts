import { NgModule } from '@angular/core';

import { ChutneyComponent } from './chutney.component';
import { RouterModule } from '@angular/router';
import { AuthGuard, authRoutes, FeatureAuthModule } from '@chutney/feature-auth';
import { AuthLayoutComponent, MainLayoutComponent, UiLayoutModule } from '@chutney/ui-layout';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [ChutneyComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: MainLayoutComponent,
        canActivate: [AuthGuard],
        children: [
          {path: '', redirectTo: 'scenarios', pathMatch: 'full'},
          {
            path: 'scenarios',
            loadChildren: () =>
              import('@chutney/feature-scenarios').then(
                (module) => module.FeatureScenariosModule
              ),
          },
          {
            path: 'environments',
            loadChildren: () =>
              import('@chutney/feature-environments').then(
                (module) => module.FeatureEnvironmentsModule
              ),
          },
        ],
      },
      {
        path: 'auth',
        component: AuthLayoutComponent,
        children: authRoutes,
      },
      {path: '**', redirectTo: 'scenarios'},
    ]),
    UiLayoutModule,
    FeatureAuthModule,
    // GraphQLModule,
  ],
  providers: [],
  exports: [CommonModule],
})
export class ChutneyModule {
}
