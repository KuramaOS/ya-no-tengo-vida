import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDepartamentoJefe } from '../departamento-jefe.model';

@Component({
  standalone: true,
  selector: 'jhi-departamento-jefe-detail',
  templateUrl: './departamento-jefe-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DepartamentoJefeDetailComponent {
  departamentoJefe = input<IDepartamentoJefe | null>(null);

  previousState(): void {
    window.history.back();
  }
}
