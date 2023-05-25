import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'app-dialog-warning[message]',
    templateUrl: './dialog-warning.component.html',
    styleUrls: ['./dialog-warning.component.sass']
})
export class DialogWarningComponent {
    @Input() open: boolean = false;
    @Input() title: string = 'Confirm your action!';
    @Input() message!: string;

    @Output('confirm') confirmCallback: EventEmitter<void> = new EventEmitter<void>();
    @Output('cancel') cancelCallback: EventEmitter<void> = new EventEmitter<void>();

    constructor() {
    }

    confirm = (): boolean => {
        this.confirmCallback.emit();
        return false;
    }

    cancel = (): boolean => {
        this.cancelCallback.emit();
        return false;
    }
}