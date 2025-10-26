export interface Transaction {
  id?: number;
  amount: number;
  type: 'Credit' | 'Debit';
  note: string;
  date?: string;
  accountId?: number;
}

export interface TransactionFilters {
  type?: 'Credit' | 'Debit';
  fromDate?: string;
  toDate?: string;
  minAmount?: number;
  maxAmount?: number;
}

export interface TransactionSort {
  field: string;
  direction: 'asc' | 'desc';
}

export interface TransactionPage {
  transactions: Transaction[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
