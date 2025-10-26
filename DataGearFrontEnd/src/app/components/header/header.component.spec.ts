import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { ThemeService } from '../../services/theme.service';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let themeService: jasmine.SpyObj<ThemeService>;

  beforeEach(async () => {
    const themeServiceSpy = jasmine.createSpyObj('ThemeService', ['toggleTheme', 'isDarkTheme']);
    themeServiceSpy.isDarkTheme.and.returnValue(false);

    await TestBed.configureTestingModule({
      imports: [HeaderComponent, ButtonModule, ToolbarModule],
      providers: [
        { provide: ThemeService, useValue: themeServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    themeService = TestBed.inject(ThemeService) as jasmine.SpyObj<ThemeService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display app name', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.title-text').textContent).toContain('Transaction Manager');
  });

  it('should call themeService.toggleTheme when theme button is clicked', () => {
    const themeButton = fixture.nativeElement.querySelector('p-button');
    themeButton.click();
    
    expect(themeService.toggleTheme).toHaveBeenCalled();
  });

  it('should show moon icon when in light theme', () => {
    themeService.isDarkTheme.and.returnValue(false);
    fixture.detectChanges();
    
    const button = fixture.nativeElement.querySelector('p-button');
    expect(button.getAttribute('icon')).toBe('pi pi-moon');
  });

  it('should show sun icon when in dark theme', () => {
    themeService.isDarkTheme.and.returnValue(true);
    fixture.detectChanges();
    
    const button = fixture.nativeElement.querySelector('p-button');
    expect(button.getAttribute('icon')).toBe('pi pi-sun');
  });
});
