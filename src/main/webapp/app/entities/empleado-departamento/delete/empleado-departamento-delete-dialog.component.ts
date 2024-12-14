import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmpleadoDepartamento } from '../empleado-departamento.model';
import { EmpleadoDepartamentoService } from '../service/empleado-departamento.service';

@Component({
  standalone: true,
  templateUrl: './empleado-departamento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmpleadoDepartamentoDeleteDialogComponent {
  empleadoDepartamento?: IEmpleadoDepartamento;

  protected empleadoDepartamentoService = inject(EmpleadoDepartamentoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empleadoDepartamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
