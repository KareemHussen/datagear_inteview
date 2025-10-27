import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap, shareReplay } from 'rxjs';
import { Transaction, TransactionFilters, TransactionSort, TransactionPage } from '../models/transaction.model';
import { Statistics } from '../models/statistics.model';
import { ApiResponse, PaginatedResponse } from '../models/api-response.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private readonly baseUrl: string;
  private lastRequestParams: any = null;
  private lastResponse$: Observable<ApiResponse<TransactionPage>> | null = null;

  constructor(private http: HttpClient) {
    this.baseUrl = environment.apiUrl;
  }

  /**
   * Get transaction statistics (total credits, debits, counts)
   */
  getStatistics(): Observable<ApiResponse<Statistics>> {
    return this.http.get<ApiResponse<Statistics>>(`${this.baseUrl}/transactions/totals`);
  }

  /**
   * Get all transactions with optional filters, sorting, and pagination
   */
  getTransactions(
    force: boolean = false,
    page: number = 0,
    size: number = 10,
    filters?: TransactionFilters,
    sort?: TransactionSort
  ): Observable<ApiResponse<TransactionPage>> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters) {
      if (filters.type) params = params.set('type', filters.type);
      if (filters.fromDate) params = params.set('fromDate', filters.fromDate);
      if (filters.toDate) params = params.set('toDate', filters.toDate);
      if (filters.minAmount) params = params.set('minAmount', filters.minAmount.toString());
      if (filters.maxAmount) params = params.set('maxAmount', filters.maxAmount.toString());
    }

    if (sort) {
      params = params.set('sortBy', `${sort.field}`);
      params = params.set('sortDirection', `${sort.direction}`);
    }

    const requestParams = { page, size, filters, sort };
    const requestKey = JSON.stringify(requestParams);

    if (!force){

      if (this.lastRequestParams === requestKey && this.lastResponse$) {
        console.log('🔁 Same request parameters — returning cached response.');
        return this.lastResponse$;
      }
    }
    

    const response$: Observable<ApiResponse<TransactionPage>> = this.http
      .get<ApiResponse<TransactionPage>>(`${this.baseUrl}/transactions`, { params })
      .pipe(
        tap(() => console.log('📡 New request sent to API')),
        shareReplay(1) // cache
      );

    this.lastRequestParams = requestKey;
    this.lastResponse$ = response$;

    return response$;
  }

  /**
   * Create a new transaction
   */
  createTransaction(transaction: Transaction): Observable<ApiResponse<Transaction>> {
    return this.http.post<ApiResponse<Transaction>>(`${this.baseUrl}/transactions`, transaction);
  }

}
