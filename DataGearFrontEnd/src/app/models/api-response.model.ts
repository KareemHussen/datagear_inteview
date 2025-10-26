export interface ValidationError {
  field: string;
  message: string;
  rejectedValue?: any;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  body: T;
  errors?: ValidationError[] | { [key: string]: string };
}

export interface PaginatedResponse<T> {
  transactions: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}
