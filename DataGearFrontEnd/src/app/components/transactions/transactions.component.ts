import { Component, OnInit, OnDestroy, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { PaginatorModule } from 'primeng/paginator';
import { TagModule } from 'primeng/tag';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { Transaction, TransactionFilters, TransactionPage, TransactionSort } from '../../models/transaction.model';
import { ApiResponse } from '../../models/api-response.model';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 
    TableModule, 
    ButtonModule, 
    InputTextModule, 
    SelectModule, 
    DatePickerModule, 
    PaginatorModule, 
    TagModule, 
    SkeletonModule, 
    MessageModule,
    FormsModule
  ],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.scss'
})
export class TransactionsComponent implements OnInit {
  protected readonly transactions = signal<Transaction[]>([]);
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly totalRecords = signal(0);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly first = signal(0);

  // Filter properties
  protected readonly filters = signal<TransactionFilters>({});
  protected readonly typeFilter = signal<string>('');
  protected readonly dateRange = signal<Date[]>([]);

  // Sort properties
  protected readonly sortField = signal<string>('createdAt');
  protected readonly sortOrder = signal<number>(-1);

  // Dropdown options
  protected readonly typeOptions = [
    { label: 'All Types', value: '' },
    { label: 'Credit', value: 'Credit' },
    { label: 'Debit', value: 'Debit' }
  ];

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.performLoad();

    const navigation = history.state;
    if (navigation && navigation.refresh) {
      this.performLoad(true);
    } else {
      this.performLoad();
    }

  }


  public performLoad(force: boolean = false): void {
    // Prevent duplicate requests
    if (this.loading()) {
      console.log('Request already in progress, skipping...');
      return;
    }

    const filters = this.buildFilters();
    const sort: TransactionSort = {
      field: this.sortField(),
      direction: this.sortOrder() === 1 ? 'ASC' : 'DESC'
    };

    this.loading.set(true);
    this.error.set(null);

    console.log('Loading transactions with filters:', filters, 'sort:', sort);

    this.transactionService.getTransactions(
      force,
      this.currentPage(),
      this.pageSize(),
      filters,
      sort
    ).subscribe({
      next: (response: ApiResponse<TransactionPage>) => {
        console.log('Transactions response received:', response);
        console.log('Transactions content:', response.body.transactions);
        console.log('Total elements:', response.body.totalElements);
        console.log('Current transactions signal before update:', this.transactions());
        
        this.transactions.set(response.body.transactions);
        this.totalRecords.set(response.body.totalElements);
        
        console.log('Transactions signal after update:', this.transactions());
        console.log('Total records signal:', this.totalRecords());
        
        this.loading.set(false);
      },
      error: (err) => {
        // Handle validation errors from API response
        if (err.error && err.error.errors) {
          const errorMessages = Object.values(err.error.errors).join(', ');
          this.error.set(`Validation errors: ${errorMessages}`);
        } else {
          this.error.set(err.error?.message || 'Failed to load transactions. Please try again.');
        }
        this.loading.set(false);
        console.error('Error loading transactions:', err);
      }
    });
  }

  private buildFilters(): TransactionFilters {
    const filters: TransactionFilters = {};

    if (this.typeFilter()) {
      filters.type = this.typeFilter() as 'Credit' | 'Debit';
    }

    if (this.dateRange().length === 2) {
      filters.fromDate = this.dateRange()[0].toISOString();
      filters.toDate = this.dateRange()[1].toISOString();
    }

    return filters;
  }

  onPageChange(event: any): void {
    this.currentPage.set(event.page);
    this.first.set(event.first);
    this.pageSize.set(event.rows);
    this.performLoad();
  }

  onSort(event: any): void {
    this.sortField.set(event.field);
    this.sortOrder.set(event.order);
    this.performLoad();
  }

  onFilterChange(): void {
    console.log('onFilterChange called');
    this.currentPage.set(0);
    this.first.set(0);
    this.performLoad();
  }

  clearFilters(): void {
    console.log('clearFilters called');
    this.typeFilter.set('');
    this.dateRange.set([]);
    this.filters.set({});
    this.currentPage.set(0);
    this.first.set(0);
    this.loading.set(false);
    this.performLoad();
  }

  getTransactionTypeSeverity(type: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' | null {
    return type === 'Credit' ? 'success' : 'danger';
  }

  getTransactionTypeLabel(type: string): string {
    return type === 'Credit' ? 'Credit' : 'Debit';
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(value);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });
  }
}
