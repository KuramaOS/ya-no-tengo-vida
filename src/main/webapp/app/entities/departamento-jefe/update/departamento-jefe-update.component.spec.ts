import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IJefe } from 'app/entities/jefe/jefe.model';
import { JefeService } from 'app/entities/jefe/service/jefe.service';
import { IDepartamentoJefe } from '../departamento-jefe.model';
import { DepartamentoJefeService } from '../service/departamento-jefe.service';
import { DepartamentoJefeFormService } from './departamento-jefe-form.service';

import { DepartamentoJefeUpdateComponent } from './departamento-jefe-update.component';

describe('DepartamentoJefe Management Update Component', () => {
  let comp: DepartamentoJefeUpdateComponent;
  let fixture: ComponentFixture<DepartamentoJefeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departamentoJefeFormService: DepartamentoJefeFormService;
  let departamentoJefeService: DepartamentoJefeService;
  let departamentoService: DepartamentoService;
  let jefeService: JefeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DepartamentoJefeUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DepartamentoJefeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartamentoJefeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departamentoJefeFormService = TestBed.inject(DepartamentoJefeFormService);
    departamentoJefeService = TestBed.inject(DepartamentoJefeService);
    departamentoService = TestBed.inject(DepartamentoService);
    jefeService = TestBed.inject(JefeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departamento query and add missing value', () => {
      const departamentoJefe: IDepartamentoJefe = { id: 456 };
      const departamento: IDepartamento = { id: 9239 };
      departamentoJefe.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 15379 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departamentoJefe });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos.map(expect.objectContaining),
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Jefe query and add missing value', () => {
      const departamentoJefe: IDepartamentoJefe = { id: 456 };
      const jefe: IJefe = { id: 11707 };
      departamentoJefe.jefe = jefe;

      const jefeCollection: IJefe[] = [{ id: 9538 }];
      jest.spyOn(jefeService, 'query').mockReturnValue(of(new HttpResponse({ body: jefeCollection })));
      const additionalJefes = [jefe];
      const expectedCollection: IJefe[] = [...additionalJefes, ...jefeCollection];
      jest.spyOn(jefeService, 'addJefeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departamentoJefe });
      comp.ngOnInit();

      expect(jefeService.query).toHaveBeenCalled();
      expect(jefeService.addJefeToCollectionIfMissing).toHaveBeenCalledWith(
        jefeCollection,
        ...additionalJefes.map(expect.objectContaining),
      );
      expect(comp.jefesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const departamentoJefe: IDepartamentoJefe = { id: 456 };
      const departamento: IDepartamento = { id: 22959 };
      departamentoJefe.departamento = departamento;
      const jefe: IJefe = { id: 19968 };
      departamentoJefe.jefe = jefe;

      activatedRoute.data = of({ departamentoJefe });
      comp.ngOnInit();

      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.jefesSharedCollection).toContain(jefe);
      expect(comp.departamentoJefe).toEqual(departamentoJefe);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamentoJefe>>();
      const departamentoJefe = { id: 123 };
      jest.spyOn(departamentoJefeFormService, 'getDepartamentoJefe').mockReturnValue(departamentoJefe);
      jest.spyOn(departamentoJefeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamentoJefe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamentoJefe }));
      saveSubject.complete();

      // THEN
      expect(departamentoJefeFormService.getDepartamentoJefe).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departamentoJefeService.update).toHaveBeenCalledWith(expect.objectContaining(departamentoJefe));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamentoJefe>>();
      const departamentoJefe = { id: 123 };
      jest.spyOn(departamentoJefeFormService, 'getDepartamentoJefe').mockReturnValue({ id: null });
      jest.spyOn(departamentoJefeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamentoJefe: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamentoJefe }));
      saveSubject.complete();

      // THEN
      expect(departamentoJefeFormService.getDepartamentoJefe).toHaveBeenCalled();
      expect(departamentoJefeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamentoJefe>>();
      const departamentoJefe = { id: 123 };
      jest.spyOn(departamentoJefeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamentoJefe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departamentoJefeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartamento', () => {
      it('Should forward to departamentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departamentoService, 'compareDepartamento');
        comp.compareDepartamento(entity, entity2);
        expect(departamentoService.compareDepartamento).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareJefe', () => {
      it('Should forward to jefeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(jefeService, 'compareJefe');
        comp.compareJefe(entity, entity2);
        expect(jefeService.compareJefe).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
