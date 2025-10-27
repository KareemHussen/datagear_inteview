import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, MenuModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  
  protected readonly menuItems: MenuItem[] = [
    {
      label: 'Statistics',
      icon: 'pi pi-chart-bar',
      routerLink: '/statistics',
    },
    {
      label: 'Transactions',
      icon: 'pi pi-list',
      routerLink: '/transactions',
    }
  ];

}
