export interface Statistics {
  totalCreditAmount: number;
  totalDebitAmount: number;
  totalCreditCount: number;
  totalDebitCount: number;
  totalTransactionCount: number;
  netAmount: number;
}

export interface StatisticsCard {
  title: string;
  value: number;
  icon: string;
  color: string;
  trend?: {
    value: number;
    direction: 'up' | 'down' | 'neutral';
  };
}
