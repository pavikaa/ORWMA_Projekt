var myChart = document.getElementById('chart1').getContext('2d');
var massPopChart = new Chart(myChart, {
    type: 'pie',
    data: {
        labels: ['Jedan', 'a', 'Tri'],
        datasets: [{
            label: 'Vrijednosti',
            data: [
                19,
                12,
                15
            ]
        }]
    },
    options: {}
});
var myChart2 = document.getElementById('chart2').getContext('2d');
var massPopChart2 = new Chart(myChart2, {
    type: 'pie',
    data: {
        labels: ['Jedan', 'Dva', 'Tri'],
        datasets: [{
            label: 'Vrijednosti',
            data: [
                19,
                12,
                15
            ]
        }]
    },
    options: {}
});
var myChart3 = document.getElementById('chart3').getContext('2d');
var massPopChart3 = new Chart(myChart3, {
    type: 'pie',
    data: {
        labels: ['Jedan', 'Dva', 'a'],
        datasets: [{
            label: 'Vrijednosti',
            data: [
                19,
                12,
                15
            ]
        }]
    },
    options: {}
});