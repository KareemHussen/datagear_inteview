import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageModule } from 'primeng/message';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';
import { TransactionService } from '../../services/transaction.service';
import { Transaction } from '../../models/transaction.model';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    CardModule, 
    ButtonModule, 
    InputTextModule, 
    InputNumberModule, 
    MessageModule,
    DialogModule,
    SelectModule,
  ],
  templateUrl: './transaction-form.component.html',
  styleUrl: './transaction-form.component.scss'
})
export class TransactionFormComponent {
  protected readonly transactionForm: FormGroup;
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly showSuccessDialog = signal(false);
  protected readonly submitted = signal(false);

  // Dropdown options
  protected readonly typeOptions = [
    { label: 'Credit', value: 'Credit' },
    { label: 'Debit', value: 'Debit' }
  ];


  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private router: Router
  ) {
    this.transactionForm = this.createForm();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      amount: [null, [Validators.required, Validators.min(0.01), Validators.max(90000000)]],
      type: ['', [Validators.required]],
      note: ['', [Validators.maxLength(255)]],
      accountId: [null]
    });
  }

  onSubmit(): void {
    this.submitted.set(true);
    this.error.set(null);

    if (this.transactionForm.valid) {
      this.loading.set(true);
      
      const formValue = this.transactionForm.value;
      const transaction: Transaction = {
        amount: formValue.amount,
        type: formValue.type,
        note: formValue.note.trim(),
        accountId: formValue.accountId
      };

      this.transactionService.createTransaction(transaction).subscribe({
        next: (response) => {
          if (response && response.success) {
            this.loading.set(false);
            this.showSuccessDialog.set(true);
          } else {
            this.error.set(response?.message || 'Failed to create transaction');
            this.loading.set(false);
          }
        },
        error: (err) => {
          // Handle validation errors from API response
          if (err.error && err.error.errors) {
            const errorMessages = Object.values(err.error.errors).join(', ');
            this.error.set(`Validation errors: ${errorMessages}`);
          } else {
            this.error.set(err.error?.message || 'Failed to create transaction. Please try again.');
          }
          this.loading.set(false);
          console.error('Error creating transaction:', err);
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  onSuccessDialogClose(): void {
    this.showSuccessDialog.set(false);
    this.router.navigate(['/transactions'], { state: { refresh: true } });
  }

  onCancel(): void {
    this.router.navigate(['/transactions'], { state: { refresh: true } });
  }

  private markFormGroupTouched(): void {
    Object.keys(this.transactionForm.controls).forEach(key => {
      const control = this.transactionForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.transactionForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched || this.submitted()));
  }

  getFieldError(fieldName: string): string {
    const field = this.transactionForm.get(fieldName);
    if (field && field.errors) {
      if (field.errors['required']) {
        return `${this.getFieldLabel(fieldName)} is required`;
      }
      if (field.errors['minlength']) {
        return `${this.getFieldLabel(fieldName)} must be at least ${field.errors['minlength'].requiredLength} characters`;
      }
      if (field.errors['maxlength']) {
        return `${this.getFieldLabel(fieldName)} must not exceed ${field.errors['maxlength'].requiredLength} characters`;
      }
      if (field.errors['min']) {
        return `${this.getFieldLabel(fieldName)} must be greater than ${field.errors['min'].min}`;
      }
      if (field.errors['max']) {
        return `${this.getFieldLabel(fieldName)} cannot exceed ${field.errors['max'].max.toLocaleString()}`;
      }
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      amount: 'Amount',
      type: 'Type',
      note: 'Note',
    };
    return labels[fieldName] || fieldName;
  }
}
