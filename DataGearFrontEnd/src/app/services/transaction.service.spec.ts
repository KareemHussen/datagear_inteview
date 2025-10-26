import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TransactionService } from './transaction.service';
import { Transaction, TransactionFilters, TransactionSort } from '../models/transaction.model';
import { Statistics } from '../models/statistics.model';

describe('TransactionService', () => {
  let service: TransactionService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TransactionService]
    });
    service = TestBed.inject(TransactionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get statistics', () => {
    const mockStatistics: Statistics = {
      totalCredits: 5000,
      totalDebits: 3000,
      creditCount: 10,
      debitCount: 8,
      totalTransactions: 18,
      netAmount: 2000
    };

    const mockResponse = {
      success: true,
      data: mockStatistics,
      message: 'Success',
      timestamp: new Date().toISOString()
    };

    service.getStatistics().subscribe(response => {
      expect(response.success).toBeTruthy();
      expect(response.data).toEqual(mockStatistics);
    });

    const req = httpMock.expectOne('http://127.0.0.1:8080/api/v1/transactions/total');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should get transactions with filters and pagination', () => {
    const mockTransactions: Transaction[] = [
      { id: 1, amount: 100, type: 'Credit', note: 'Test', date: '2024-01-01' }
    ];

    const mockResponse = {
      content: mockTransactions,
      totalElements: 1,
      totalPages: 1,
      size: 10,
      number: 0,
      first: true,
      last: true,
      numberOfElements: 1,
      empty: false
    };

    const filters: TransactionFilters = { type: 'Credit' };
    const sort: TransactionSort = { field: 'date', direction: 'desc' };

    service.getTransactions(0, 10, filters, sort).subscribe(response => {
      expect(response.content).toEqual(mockTransactions);
      expect(response.totalElements).toBe(1);
    });

    const req = httpMock.expectOne('http://127.0.0.1:8080/api/v1/transactions?page=0&size=10&type=Credit&sort=date,desc');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should create transaction', () => {
    const newTransaction: Transaction = {
      amount: 100,
      type: 'Credit',
      denotescription: 'Test transaction',
      date: '2024-01-01'
    };

    const mockResponse = {
      success: true,
      data: { ...newTransaction, id: 1 },
      message: 'Transaction created successfully',
      timestamp: new Date().toISOString()
    };

    service.createTransaction(newTransaction).subscribe(response => {
      expect(response.success).toBeTruthy();
      expect(response.data.id).toBe(1);
    });

    const req = httpMock.expectOne('http://127.0.0.1:8080/api/v1/transactions');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newTransaction);
    req.flush(mockResponse);
  });

  it('should get transaction by ID', () => {
    const mockTransaction: Transaction = {
      id: 1,
      amount: 100,
      type: 'Credit',
      note: 'Test',
      date: '2024-01-01'
    };

    const mockResponse = {
      success: true,
      data: mockTransaction,
      message: 'Success',
      timestamp: new Date().toISOString()
    };

    service.getTransactionById(1).subscribe(response => {
      expect(response.success).toBeTruthy();
      expect(response.data).toEqual(mockTransaction);
    });

    const req = httpMock.expectOne('http://127.0.0.1:8080/api/v1/transactions/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should update transaction', () => {
    const updatedTransaction: Transaction = {
      id: 1,
      amount: 150,
      type: 'Credit',
      note: 'Updated transaction',
      date: '2024-01-01'
    };

    const mockResponse = {
      success: true,
      data: updatedTransaction,
      message: 'Transaction updated successfully',
      timestamp: new Date().toISOString()
    };

    service.updateTransaction(1, updatedTransaction).subscribe(response => {
      expect(response.success).toBeTruthy();
      expect(response.data).toEqual(updatedTransaction);
    });

    const req = httpMock.expectOne('http://127.0.0.1:8080/api/v1/transactions/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedTransaction);
    req.flush(mockResponse);
  });

  it('should delete transaction', () => {
    const mockResponse = {
      success: true,
      data: null,
      message: 'Transaction deleted successfully',
      timestamp: new Date().toISOString()
    };

    service.deleteTransaction(1).subscribe(response => {
      expect(response.success).toBeTruthy();
    });

    const req = httpMock.expectOne('http://127.0.0.1:8080/api/v1/transactions/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(mockResponse);
  });
});
