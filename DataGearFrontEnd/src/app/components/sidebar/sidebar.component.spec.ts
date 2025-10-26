import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SidebarComponent } from './sidebar.component';
import { TabMenuModule } from 'primeng/tabmenu';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarComponent, RouterTestingModule, TabMenuModule]
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have two menu items', () => {
    expect(component.menuItems).toHaveSize(2);
    expect(component.menuItems[0].label).toBe('Statistics');
    expect(component.menuItems[1].label).toBe('Transactions');
  });

  it('should set active tab when setActiveTab is called', () => {
    component.setActiveTab(1);
    expect(component.activeTab()).toBe(1);
  });

  it('should have correct router links', () => {
    expect(component.menuItems[0].routerLink).toBe('/statistics');
    expect(component.menuItems[1].routerLink).toBe('/transactions');
  });

  it('should have correct icons', () => {
    expect(component.menuItems[0].icon).toBe('pi pi-chart-bar');
    expect(component.menuItems[1].icon).toBe('pi pi-list');
  });
});
