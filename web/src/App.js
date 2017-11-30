import React, {Component} from 'react';
import NavbarComponent from './components/navbar';
import ChartComponent from './components/chart';
import TableComponent from './components/table';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tweets: [],
            chart: []
        };
    }

    componentDidMount() {
        const evtSource = new EventSource("http://localhost:8000/streaming/tweets");

        evtSource.onmessage = function(e) {
            if (e.data.length > 0) {
                let data = JSON.parse(e.data);
                let negative = data.filter((entry) => { return entry.sentiment > 0; });
                let chartEntry = {
                    'total': data.length,
                    'negative': negative.length
                };
                let chart = this.state.chart;
                if (chart.length > 14) {
                    chart.shift();
                }
                chart.push(chartEntry);

                let newState = Object.assign({}, this.state, { tweets: data, chart: chart });
                this.setState(newState);
            }
        }.bind(this)
    }

    render() {
        return (
            <div className="container-fluid">
                <NavbarComponent />
                <div className="row">
                    <div className="col-md-6 col-sm-12">
                        <TableComponent tweets={this.state.tweets} />
                    </div>
                    <div className="col-md-6 col-sm-12">
                        <ChartComponent chart={this.state.chart} />
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
