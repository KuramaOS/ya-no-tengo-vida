import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDepartamentoJefe } from '../departamento-jefe.model';
import { DepartamentoJefeService } from '../service/departamento-jefe.service';

@Component({
  standalone: true,
  templateUrl: './departamento-jefe-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DepartamentoJefeDeleteDialogComponent {
  departamentoJefe?: IDepartamentoJefe;

  protected departamentoJefeService = inject(DepartamentoJefeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departamentoJefeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
