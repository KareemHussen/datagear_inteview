import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageModule } from 'primeng/message';
import { ButtonModule } from 'primeng/button';
import { TransactionService } from '../../services/transaction.service';
import { Statistics } from '../../models/statistics.model';
import { StatisticsCard } from '../../models/statistics.model';

@Component({
  selector: 'app-statistics',
  standalone: true,
  imports: [CommonModule, CardModule, SkeletonModule, MessageModule, ButtonModule],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.scss'
})
export class StatisticsComponent implements OnInit {
  protected readonly statistics = signal<Statistics | null>(null);
  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly cards = signal<StatisticsCard[]>([]);

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  private loadStatistics(): void {
    this.loading.set(true);
    this.error.set(null);

    this.transactionService.getStatistics().subscribe({
      next: (response) => {
        console.log('Statistics response:', response);
        
        // Handle new API response structure
        let statisticsData: Statistics;
        
        if (response && response.success && response.body) {
          // New API response structure
          statisticsData = response.body;
          console.log('Statistics data:', statisticsData);
        } else if (response && typeof response === 'object' && !response.success) {
          // Direct data response (no wrapper)
          statisticsData = response as any;
          console.log('Statistics data (direct):', statisticsData);
        } else {
          this.error.set(response?.message || 'Invalid response format');
          this.loading.set(false);
          return;
        }

        // Validate that we have the required statistics data
        if (statisticsData && typeof statisticsData === 'object') {
          this.statistics.set(statisticsData);
          this.createCards(statisticsData);
        } else {
          this.error.set('Invalid statistics data format');
        }
        
        this.loading.set(false);
      },
      error: (err) => {
        // Handle validation errors from API response
        if (err.error && err.error.errors) {
          const errorMessages = Object.values(err.error.errors).join(', ');
          this.error.set(`Validation errors: ${errorMessages}`);
        } else {
          this.error.set(err.error?.message || 'Failed to load statistics. Please try again.');
        }
        this.loading.set(false);
        console.error('Error loading statistics:', err);
      }
    });
  }

  private createCards(stats: Statistics): void {
    const cards: StatisticsCard[] = [
      {
        title: 'Total Credits',
        value: stats.totalCreditAmount,
        icon: 'pi pi-arrow-up',
        color: 'green',
        trend: {
          value: stats.totalCreditCount,
          direction: 'up'
        }
      },
      {
        title: 'Total Debits',
        value: stats.totalDebitAmount,
        icon: 'pi pi-arrow-down',
        color: 'red',
        trend: {
          value: stats.totalDebitCount,
          direction: 'down'
        }
      },
      {
        title: 'Total Transactions',
        value: stats.totalDebitAmount + stats.totalCreditAmount,
        icon: 'pi pi-list',
        color: 'blue',
        trend: {
          value: stats.totalTransactionCount,
          direction: 'neutral'
        }
      },
      {
        title: 'Net Amount',
        value: stats.netAmount,
        icon: stats.netAmount >= 0 ? 'pi pi-arrow-up' : 'pi pi-arrow-down',
        color: stats.netAmount >= 0 ? 'green' : 'red',
        trend: {
          value: Math.abs(stats.netAmount),
          direction: stats.netAmount >= 0 ? 'up' : 'down'
        }
      }
    ];

    this.cards.set(cards);
  }

  refreshStatistics(): void {
    this.loadStatistics();
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(value);
  }
}
