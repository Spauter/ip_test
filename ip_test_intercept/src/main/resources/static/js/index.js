const myChart = echarts.init(document.getElementById('main'));
function init() {
    axios.get('../intercept/current_count', {
        headers: {
            'diy_name': 'Bloduc Spauter'
        }
    })
        .then(response => {
            const seriesData = response.data;

           const option= {
                legend: {
                    data: ['请求数', '拦截数']
                },
                xAxis: {
                    type: 'category',
                    name: '时间',
                    data: ['60', '55', '50', '45', '40', '35', '30', '25', '20', '15', '10','5']
                },
                yAxis: {
                    type: 'value'
                },
                series: seriesData,
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    }
                },
            };

            myChart.setOption(option);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}
setInterval(init,5000)

