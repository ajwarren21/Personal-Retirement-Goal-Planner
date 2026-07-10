import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RetirementGoalsComponent } from './retirement-goals.component';

describe('RetirementGoalsComponent', () => {
  let component: RetirementGoalsComponent;
  let fixture: ComponentFixture<RetirementGoalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RetirementGoalsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RetirementGoalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
