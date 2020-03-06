window.mdc.autoInit();

const pageSize = 50;
const defaultMin = 0;
const defaultMax = 32767; // 32 bit int max in C

let count = 0;
const cellTextStyle = 'mdc-typography--headline3'
const container = document.querySelector('#quiz-container')

function handleQuizResponse(resp) {
    console.log(`size=${resp.length}, ${JSON.stringify(resp)}`);

    var i;
    let cell;
    for(i = 0; i < resp.length; i++) {
        cell = document.createElement('div');
        cell.setAttribute('class', `mdc-layout-grid__cell ${cellTextStyle}`);
        cell.textContent = resp[i];
        container.appendChild(cell);
    }

    count = count + resp.length;
    document.querySelector('#quiz-count').textContent = count;
}

document.querySelector('#action-generate').onclick = function() {
    const min = document.querySelector('#input-text-min').value;
    const max = document.querySelector('#input-text-max').value;
    const plusEnabled = document.querySelector('#checkbox-plus').checked;
    const minusEnabled = document.querySelector('#checkbox-minus').checked;

    console.log(`min=${min}, max=${max}, plusEnabled=${plusEnabled}, minusEnabled=${minusEnabled}`);

    var url = "api/quiz?";

    if(min) 
        url = url + `min=${min}`;
    else
        url = url + `min=${defaultMin}`;

    if(max) 
        url = url + `&max=${max}`;
    else
        url = url + `&max=${defaultMax}`;

    url = url + `&size=${pageSize}`;

    if(plusEnabled) url = url + `&operators=Plus`;
    if(minusEnabled) url = url + `&operators=Minus`;

    fetch(url)
        .then(resp => { return resp.json(); })
        .then(json => { handleQuizResponse(json); });
};
