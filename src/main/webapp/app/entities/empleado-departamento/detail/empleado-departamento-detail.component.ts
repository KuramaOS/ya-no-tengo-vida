import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IEmpleadoDepartamento } from '../empleado-departamento.model';

@Component({
  standalone: true,
  selector: 'jhi-empleado-departamento-detail',
  templateUrl: './empleado-departamento-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EmpleadoDepartamentoDetailComponent {
  empleadoDepartamento = input<IEmpleadoDepartamento | null>(null);

  previousState(): void {
    window.history.back();
  }
}
