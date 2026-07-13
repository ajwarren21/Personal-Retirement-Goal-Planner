import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GoalDetailComponent } from './goal-detail.component';

describe('GoalDetailComponent', () => {
  let component: GoalDetailComponent;
  let fixture: ComponentFixture<GoalDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoalDetailComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(GoalDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
