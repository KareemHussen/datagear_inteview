import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { StatisticsComponent } from './statistics.component';
import { TransactionService } from '../../services/transaction.service';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';
import { ButtonModule } from 'primeng/button';

describe('StatisticsComponent', () => {
  let component: StatisticsComponent;
  let fixture: ComponentFixture<StatisticsComponent>;
  let transactionService: jasmine.SpyObj<TransactionService>;

  const mockStatistics = {
    totalCredits: 5000,
    totalDebits: 3000,
    creditCount: 10,
    debitCount: 8,
    totalTransactions: 18,
    netAmount: 2000
  };

  beforeEach(async () => {
    const transactionServiceSpy = jasmine.createSpyObj('TransactionService', ['getStatistics']);

    await TestBed.configureTestingModule({
      imports: [StatisticsComponent, CardModule, SkeletonModule, MessageModule, ButtonModule],
      providers: [
        { provide: TransactionService, useValue: transactionServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(StatisticsComponent);
    component = fixture.componentInstance;
    transactionService = TestBed.inject(TransactionService) as jasmine.SpyObj<TransactionService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load statistics on init', () => {
    transactionService.getStatistics.and.returnValue(of({
      success: true,
      data: mockStatistics,
      message: 'Success',
      timestamp: new Date().toISOString()
    }));

    component.ngOnInit();
    fixture.detectChanges();

    expect(transactionService.getStatistics).toHaveBeenCalled();
    expect(component['statistics']()).toEqual(mockStatistics);
    expect(component['loading']()).toBeFalse();
  });

  it('should handle error when loading statistics', () => {
    transactionService.getStatistics.and.returnValue(throwError(() => new Error('API Error')));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component['error']()).toBe('Failed to load statistics. Please try again.');
    expect(component['loading']()).toBeFalse();
  });

  it('should create cards from statistics data', () => {
    transactionService.getStatistics.and.returnValue(of({
      success: true,
      data: mockStatistics,
      message: 'Success',
      timestamp: new Date().toISOString()
    }));

    component.ngOnInit();
    fixture.detectChanges();

    const cards = component['cards']();
    expect(cards).toHaveSize(4);
    expect(cards[0].title).toBe('Total Credits');
    expect(cards[0].value).toBe(5000);
    expect(cards[1].title).toBe('Total Debits');
    expect(cards[1].value).toBe(3000);
  });

  it('should format currency correctly', () => {
    const formatted = component.formatCurrency(1234.56);
    expect(formatted).toContain('$1,234.56');
  });

  it('should refresh statistics when refresh button is clicked', () => {
    transactionService.getStatistics.and.returnValue(of({
      success: true,
      data: mockStatistics,
      message: 'Success',
      timestamp: new Date().toISOString()
    }));

    component.refreshStatistics();
    expect(transactionService.getStatistics).toHaveBeenCalledTimes(1);
  });
});
