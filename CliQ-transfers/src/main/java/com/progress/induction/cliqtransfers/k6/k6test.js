import http from 'k6/http';
import { check } from 'k6';

export let options = {
    vus: 50,
    duration: '10s',
};

export default function () {
    const payload = JSON.stringify({
        "debitAccount": "0000122204115301",
        "amount": 10,
        "beneficiaryAccount": "testAlias",
        "beneficiaryType": "Alias",
        "date": "2024-10-24"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let res = http.post('http://localhost:8080/v1/transfers', payload, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}

/*
* install k6 if u dont have it
* go to this dir: /cliq-with-helm/C/s/main/java/com/progress/induction/cliqtransfers/k6
* use this command to run the code: k6 run k6test.js
* */
