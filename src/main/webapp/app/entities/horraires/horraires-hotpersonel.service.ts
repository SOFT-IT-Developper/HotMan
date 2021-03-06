import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { HorrairesHotpersonel } from './horraires-hotpersonel.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class HorrairesHotpersonelService {

    private resourceUrl = 'api/horraires';
    private resourceSearchUrl = 'api/_search/horraires';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(horraires: HorrairesHotpersonel): Observable<HorrairesHotpersonel> {
        const copy = this.convert(horraires);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(horraires: HorrairesHotpersonel): Observable<HorrairesHotpersonel> {
        const copy = this.convert(horraires);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<HorrairesHotpersonel> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.startDate = this.dateUtils
            .convertDateTimeFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertDateTimeFromServer(entity.endDate);
    }

    private convert(horraires: HorrairesHotpersonel): HorrairesHotpersonel {
        const copy: HorrairesHotpersonel = Object.assign({}, horraires);

        copy.startDate = this.dateUtils.toDate(horraires.startDate);

        copy.endDate = this.dateUtils.toDate(horraires.endDate);
        return copy;
    }
}
