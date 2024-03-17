import {
  HttpClient
} from '@angular/common/http';
import {
  Component,
  OnInit
} from '@angular/core';
import {
  FormBuilder,Validators,FormControl
} from '@angular/forms';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  Subscription
} from 'rxjs';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  environment
} from 'src/environments/environment';
import {
  FormService
} from '../../form.service';
import {
  FeedbackService
} from '../feedback.service';
import { IntakeService } from 'src/app/main-module/crm/intake-snap-main/intake.service';

@Component({
  selector: 'app-english-feedback',
  templateUrl: './english-feedback.component.html',
  styleUrls: ['./english-feedback.component.scss']
})
export class EnglishFeedbackComponent implements OnInit {

  form = this.fb.group({
    attorneyConsultation: [, [Validators.required]],
    attorneyDiscussionComfortable: [, [Validators.required]],
    classId: [],
    consultationInformative: [, [Validators.required]],
    consultationSchedule: [, [Validators.required]],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    firstPersonInteraction: [, [Validators.required]],
    improveRemark: [, [Validators.required]],
    inquiryExperience: [, [Validators.required]],
    inquiryNumber: [],
    intakeFormNumber: [],
    languageId: [],
    overallExperience: [, [Validators.required]],
    referFriendOrFamily: [, [Validators.required]],
    referenceField1: [],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    staffFollowUp: [, [Validators.required]],
    statusId: [],
    updatedBy: [],
    updatedOn: [],

  });
  js: any;
  constructor(private fb: FormBuilder, private auth: AuthService, private cs: CommonService, private route: ActivatedRoute,
    private fservice: FormService,
    private router: Router,
    private spin: NgxSpinnerService, public toastr: ToastrService, private serivceIntake: IntakeService,
    private http: HttpClient,
    private cas: CommonApiService, private service: FeedbackService) {}

  sub = new Subscription();
  ngOnInit(): void {

    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);

    console.log(this.js);
    this.sub.add(
      this.http.get < any > (`/mnr-setup-service/login?userId=` + 'RS' + `&password=` + 'MR@2022').subscribe(
        (res) => {
          this.spin.hide();
          //   let menu = [1000, 1001, 1004, 2101, 2102, 2202, 2203];
          //   sessionStorage.setItem('menu', menu.toString());
          sessionStorage.setItem("user", JSON.stringify(res))
        },
        (rej) => {
          this.spin.hide();
          this.toastr.error("", rej);
          this.form.disable();
        }
      )
    );



  }
  submitted = false;
  email = new FormControl('', [Validators.required, Validators.email]);
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  back() {

  }
 
  submit() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      )
     this.cs.notifyOther(true);
      return;
    }
    this.cs.notifyOther(false);
    this.spin.show();
    //this.form.controls.statusId.patchValue(59);
    this.form.controls.classId.patchValue(this.js.classId);
    this.form.controls.inquiryNumber.patchValue(this.js.inquiryNumber);
    this.form.controls.intakeFormNumber.patchValue(this.js.intakeFormNumber);
    this.form.controls.languageId.patchValue(this.js.languageId);

    let obj1: any = {};
    obj1.feedbackStatus = "Form Received";
    obj1.statusId = this.js.statusId;

    this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success("Survey form submitted Sucessfully! ", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.sub.add(this.serivceIntake.Update(obj1, this.form.controls.intakeFormNumber.value).subscribe(res => {

      }, err =>{
        this.cs.commonerror(err);
        this.spin.hide();
      }));
      this.spin.hide();
      this.router.navigate(['/mr/feedbackThanks']);
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    })
  }

  
}
