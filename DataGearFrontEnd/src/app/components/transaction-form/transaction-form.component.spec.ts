import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { TransactionFormComponent } from './transaction-form.component';
import { TransactionService } from '../../services/transaction.service';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { MessageModule } from 'primeng/message';
import { DialogModule } from 'primeng/dialog';

describe('TransactionFormComponent', () => {
  let component: TransactionFormComponent;
  let fixture: ComponentFixture<TransactionFormComponent>;
  let transactionService: jasmine.SpyObj<TransactionService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const transactionServiceSpy = jasmine.createSpyObj('TransactionService', ['createTransaction']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        TransactionFormComponent, 
        ReactiveFormsModule,
        CardModule,
        ButtonModule,
        InputTextModule,
        InputNumberModule,
        DropdownModule,
        CalendarModule,
        MessageModule,
        DialogModule
      ],
      providers: [
        { provide: TransactionService, useValue: transactionServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TransactionFormComponent);
    component = fixture.componentInstance;
    transactionService = TestBed.inject(TransactionService) as jasmine.SpyObj<TransactionService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with default values', () => {
    expect(component.transactionForm).toBeDefined();
    expect(component.transactionForm.get('amount')).toBeDefined();
    expect(component.transactionForm.get('type')).toBeDefined();
    expect(component.transactionForm.get('note')).toBeDefined();
    expect(component.transactionForm.get('date')).toBeDefined();
  });

  it('should set default date on init', () => {
    component.ngOnInit();
    expect(component.transactionForm.get('date')?.value).toBeDefined();
  });

  it('should validate required fields', () => {
    component.onSubmit();
    
    expect(component.transactionForm.get('amount')?.invalid).toBeTruthy();
    expect(component.transactionForm.get('type')?.invalid).toBeTruthy();
    expect(component.transactionForm.get('note')?.invalid).toBeTruthy();
    expect(component.transactionForm.get('date')?.invalid).toBeTruthy();
  });

  it('should create transaction when form is valid', () => {
    const mockResponse = {
      success: true,
      data: { id: 1, amount: 100, type: 'Credit', note: 'Test', date: '2024-01-01' },
      message: 'Success',
      timestamp: new Date().toISOString()
    };

    transactionService.createTransaction.and.returnValue(of(mockResponse));

    component.transactionForm.patchValue({
      amount: 100,
      type: 'Credit',
      note: 'Test transaction',
      date: new Date()
    });

    component.onSubmit();

    expect(transactionService.createTransaction).toHaveBeenCalled();
    expect(component.loading()).toBeFalse();
  });

  it('should handle error when creating transaction', () => {
    transactionService.createTransaction.and.returnValue(throwError(() => new Error('API Error')));

    component.transactionForm.patchValue({
      amount: 100,
      type: 'Credit',
      note: 'Test transaction',
      date: new Date()
    });

    component.onSubmit();

    expect(component.error()).toBe('Failed to create transaction. Please try again.');
    expect(component.loading()).toBeFalse();
  });

  it('should navigate to transactions on cancel', () => {
    component.onCancel();
    expect(router.navigate).toHaveBeenCalledWith(['/transactions']);
  });

  it('should navigate to transactions after successful creation', () => {
    component.onSuccessDialogClose();
    expect(router.navigate).toHaveBeenCalledWith(['/transactions']);
  });

  it('should detect invalid fields', () => {
    component.transactionForm.patchValue({
      amount: null,
      type: '',
      note: '',
      date: null
    });

    expect(component.isFieldInvalid('amount')).toBeTruthy();
    expect(component.isFieldInvalid('type')).toBeTruthy();
    expect(component.isFieldInvalid('note')).toBeTruthy();
    expect(component.isFieldInvalid('date')).toBeTruthy();
  });

  it('should get field errors', () => {
    component.transactionForm.get('amount')?.setErrors({ required: true });
    expect(component.getFieldError('amount')).toBe('Amount is required');

    component.transactionForm.get('note')?.setErrors({ minlength: { requiredLength: 3 } });
    expect(component.getFieldError('note')).toBe('note must be at least 3 characters');
  });

  it('should validate minimum amount', () => {
    component.transactionForm.patchValue({ amount: 0 });
    expect(component.transactionForm.get('amount')?.invalid).toBeTruthy();

    component.transactionForm.patchValue({ amount: 0.01 });
    expect(component.transactionForm.get('amount')?.valid).toBeTruthy();
  });

  it('should validate note length', () => {
    component.transactionForm.patchValue({ note: 'ab' });
    expect(component.transactionForm.get('note')?.invalid).toBeTruthy();

    component.transactionForm.patchValue({ note: 'Valid note' });
    expect(component.transactionForm.get('note')?.valid).toBeTruthy();
  });
});
