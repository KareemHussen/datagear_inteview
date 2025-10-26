import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { TransactionsComponent } from './transactions.component';
import { TransactionService } from '../../services/transaction.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { PaginatorModule } from 'primeng/paginator';
import { TagModule } from 'primeng/tag';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';

describe('TransactionsComponent', () => {
  let component: TransactionsComponent;
  let fixture: ComponentFixture<TransactionsComponent>;
  let transactionService: jasmine.SpyObj<TransactionService>;

  const mockTransactions = [
    {
      id: 1,
      amount: 100,
      type: 'Credit',
      note: 'Test credit',
      date: '2024-01-01',
      category: 'FOOD'
    },
    {
      id: 2,
      amount: 50,
      type: 'Debit',
      note: 'Test debit',
      date: '2024-01-02',
      category: 'TRANSPORTATION'
    }
  ];

  const mockPaginatedResponse = {
    content: mockTransactions,
    totalElements: 2,
    totalPages: 1,
    size: 10,
    number: 0,
    first: true,
    last: true,
    numberOfElements: 2,
    empty: false
  };

  beforeEach(async () => {
    const transactionServiceSpy = jasmine.createSpyObj('TransactionService', ['getTransactions']);

    await TestBed.configureTestingModule({
      imports: [
        TransactionsComponent, 
        RouterTestingModule, 
        FormsModule,
        TableModule,
        ButtonModule,
        InputTextModule,
        DropdownModule,
        CalendarModule,
        PaginatorModule,
        TagModule,
        SkeletonModule,
        MessageModule
      ],
      providers: [
        { provide: TransactionService, useValue: transactionServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TransactionsComponent);
    component = fixture.componentInstance;
    transactionService = TestBed.inject(TransactionService) as jasmine.SpyObj<TransactionService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load transactions on init', () => {
    transactionService.getTransactions.and.returnValue(of(mockPaginatedResponse));

    component.ngOnInit();
    fixture.detectChanges();

    expect(transactionService.getTransactions).toHaveBeenCalled();
    expect(component.transactions()).toEqual(mockTransactions);
    expect(component.totalRecords()).toBe(2);
    expect(component.loading()).toBeFalse();
  });

  it('should handle error when loading transactions', () => {
    transactionService.getTransactions.and.returnValue(throwError(() => new Error('API Error')));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.error()).toBe('Failed to load transactions. Please try again.');
    expect(component.loading()).toBeFalse();
  });

  it('should apply filters when filter changes', () => {
    transactionService.getTransactions.and.returnValue(of(mockPaginatedResponse));
    component.ngOnInit();

    component.typeFilter.set('Credit');
    component.onFilterChange();

    expect(transactionService.getTransactions).toHaveBeenCalledWith(
      0, 10, jasmine.objectContaining({ type: 'Credit' }), jasmine.any(Object)
    );
  });

  it('should clear filters', () => {
    component.searchText.set('test');
    component.typeFilter.set('Credit');
    component.categoryFilter.set('FOOD');
    component.dateRange.set([new Date(), new Date()]);

    component.clearFilters();

    expect(component.searchText()).toBe('');
    expect(component.typeFilter()).toBe('');
    expect(component.categoryFilter()).toBe('');
    expect(component.dateRange()).toEqual([]);
  });

  it('should handle page change', () => {
    transactionService.getTransactions.and.returnValue(of(mockPaginatedResponse));
    component.ngOnInit();

    const pageEvent = { page: 1, first: 10, rows: 10 };
    component.onPageChange(pageEvent);

    expect(component.currentPage()).toBe(1);
    expect(component.first()).toBe(10);
    expect(component.pageSize()).toBe(10);
  });

  it('should handle sort change', () => {
    transactionService.getTransactions.and.returnValue(of(mockPaginatedResponse));
    component.ngOnInit();

    const sortEvent = { field: 'amount', order: 1 };
    component.onSort(sortEvent);

    expect(component.sortField()).toBe('amount');
    expect(component.sortOrder()).toBe(1);
  });

  it('should format currency correctly', () => {
    const formatted = component.formatCurrency(1234.56);
    expect(formatted).toContain('$1,234.56');
  });

  it('should format date correctly', () => {
    const formatted = component.formatDate('2024-01-01');
    expect(formatted).toContain('Jan 1, 2024');
  });

  it('should get correct transaction type severity', () => {
    expect(component.getTransactionTypeSeverity('Credit')).toBe('success');
    expect(component.getTransactionTypeSeverity('Debit')).toBe('danger');
  });

  it('should get correct transaction type label', () => {
    expect(component.getTransactionTypeLabel('Credit')).toBe('Credit');
    expect(component.getTransactionTypeLabel('Debit')).toBe('Debit');
  });
});
