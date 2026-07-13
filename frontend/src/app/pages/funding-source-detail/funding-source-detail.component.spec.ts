import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FundingSourceDetailComponent } from './funding-source-detail.component';

describe('FundingSourceDetailComponent', () => {
  let component: FundingSourceDetailComponent;
  let fixture: ComponentFixture<FundingSourceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FundingSourceDetailComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FundingSourceDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
